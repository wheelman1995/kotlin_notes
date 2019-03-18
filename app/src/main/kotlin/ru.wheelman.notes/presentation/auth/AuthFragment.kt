package ru.wheelman.notes.presentation.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import dagger.Lazy
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentAuthBinding
import ru.wheelman.notes.model.entities.AuthMode.*
import ru.wheelman.notes.model.entities.CurrentUser
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.utils.GoogleSignInHelper
import ru.wheelman.notes.presentation.utils.PreferenceHelper
import javax.inject.Inject

class AuthFragment : Fragment() {

    private companion object {
        private const val RC_SIGN_IN = 8735
        private const val RC_PERMISSION = 5981
    }

    @Inject
    internal lateinit var currentUser: CurrentUser
    @Inject
    internal lateinit var appContext: Context
    @Inject
    internal lateinit var preferenceHelper: PreferenceHelper
    @Inject
    internal lateinit var googleSignInHelper: Lazy<GoogleSignInHelper>
    private lateinit var viewModel: AuthFragmentViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAuthBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        initDagger()
        initVariables()
        checkAuth()
        initBinding()
        hideToolbar()
        return binding.root
    }

    private fun hideToolbar() {
        mainActivity.supportActionBar?.hide()
    }

    private fun restoreToolbar() {
        mainActivity.supportActionBar?.show()
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun checkAuth() {
        when (preferenceHelper.getAuthMode()) {
            GOOGLE -> tryAuthorizingUserWithGoogle()
            GUEST -> tryAuthorizingUserAsGuest()
            UNAUTHORIZED -> return
        }
    }

    private fun tryAuthorizingUserWithGoogle() {
        val lastSignedInAccount = googleSignInHelper.get().lastSignedInAccount
        if (lastSignedInAccount != null) {
            authorizeUserWithGoogle(lastSignedInAccount)
        } else {
            startActivityForResult(
                googleSignInHelper.get().client.signInIntent,
                RC_SIGN_IN
            )
        }
    }

    private fun authorizeUserWithGoogle(account: GoogleSignInAccount) {
        preferenceHelper.putAuthMode(GOOGLE)
        currentUser.id = account.id!!
        goToMainFragment()
    }

    private fun goToMainFragment() {
        navController.navigate(R.id.action_authFragment_to_mainFragment)
    }

    fun signInWithGoogle(view: View) {
        tryAuthorizingUserWithGoogle()
    }

    private fun tryAuthorizingUserAsGuest() {
        if (permissionGranted()) {
            authorizeUserAsGuest()
        } else {
            requestPermission()
        }
    }

    private fun authorizeUserAsGuest() {
        preferenceHelper.putAuthMode(GUEST)
        val telephonyManager =
            appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        @SuppressLint("MissingPermission")
        val deviceId = telephonyManager.deviceId
        currentUser.id = deviceId
        goToMainFragment()
    }

    fun signInAsGuest(view: View) {
        tryAuthorizingUserAsGuest()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), RC_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                authorizeUserAsGuest()
            } else {
                showSnackbar(getString(R.string.phone_permission_denied))
            }
        }
    }

    private fun permissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            if (account != null) {
                authorizeUserWithGoogle(account)
            } else {
                showSnackbar(getString(R.string.could_not_sign_in_with_google))
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    private fun initVariables() {
        viewModel = ViewModelProviders.of(this).get(AuthFragmentViewModel::class.java)
        navController = findNavController()
        mainActivity = activity as MainActivity
    }

    override fun onDestroyView() {
        restoreToolbar()
        super.onDestroyView()
    }
}