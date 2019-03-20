package ru.wheelman.notes.presentation.auth

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.SingleSubject
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentAuthBinding
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.utils.ActivityResult
import ru.wheelman.notes.presentation.utils.Authenticator
import javax.inject.Inject

class AuthFragment : Fragment() {

    private companion object {
        private const val RC_PERMISSION = 5981
    }

    @Inject
    internal lateinit var authenticator: Authenticator
    private lateinit var viewModel: AuthFragmentViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAuthBinding
    private lateinit var mainActivity: MainActivity
    private val onAuthSucceeded = { goToMainFragment() }
    private val onAuthFailed: (Throwable) -> Unit = { showSnackbar(it.message) }
    private lateinit var permissionResultEmitter: CompletableEmitter
    private val checkPermission: (String) -> Completable = { permission ->
        CompletableSubject.create { emitter ->
            permissionResultEmitter = emitter
            if (permissionGranted(permission)) emitter.onComplete()
            else requestPermission(permission)
        }
    }
    private lateinit var activityResultEmitter: SingleEmitter<ActivityResult>
    private val startActivityForResult: (Intent, Int) -> Single<ActivityResult> = { intent, rc ->
        SingleSubject.create<ActivityResult> { emitter ->
            activityResultEmitter = emitter
            startActivityForResult(intent, rc)
        }
    }
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        initDagger()
        initVariables()
        tryToAuthenticate()
        initBinding()
        hideToolbar()
        return binding.root
    }

    private fun tryToAuthenticate() {
        val d = authenticator.tryToAuthenticate(
            startActivityForResult,
            checkPermission
        ).subscribe(
            onAuthSucceeded,
            onAuthFailed
        )
        disposable.add(d)
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

    private fun goToMainFragment() {
        navController.navigate(R.id.action_authFragment_to_mainFragment)
    }

    fun signInWithGoogle(view: View) {
        val d = authenticator.tryAuthorizingUserWithGoogle(startActivityForResult).subscribe(
            onAuthSucceeded,
            onAuthFailed
        )
        disposable.add(d)
    }

    fun signInAsGuest(view: View) {
        val d = authenticator.tryAuthorizingUserAsGuest(checkPermission).subscribe(
            onAuthSucceeded,
            onAuthFailed
        )
        disposable.add(d)
    }

    private fun requestPermission(permission: String) {
        requestPermissions(arrayOf(permission), RC_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permissionResultEmitter.onComplete()
            } else {
                permissionResultEmitter.onError(Throwable("the permission was denied"))
            }
        }
    }

    private fun permissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultEmitter.onSuccess(ActivityResult(requestCode, resultCode, data))
    }

    private fun showSnackbar(message: String?) {
        Snackbar.make(requireView(), message ?: "Unknown error", Snackbar.LENGTH_LONG).show()
    }

    private fun initVariables() {
        viewModel = ViewModelProviders.of(this).get(AuthFragmentViewModel::class.java)
        navController = findNavController()
        mainActivity = activity as MainActivity
    }

    override fun onDestroyView() {
        disposable.dispose()
        authenticator.onDestroyView()
        restoreToolbar()
        super.onDestroyView()
    }
}