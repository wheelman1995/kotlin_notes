package ru.wheelman.notes.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.ActivityMainBinding
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.utils.Authenticator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var authenticator: Authenticator
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initDagger()
        initVariables()
        initActionBar()
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(
            navController,
            AppBarConfiguration(setOf(R.id.authFragment, R.id.mainFragment))
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item?.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun logout() {
        authenticator.logout()
        navController.navigate(R.id.to_authFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    private fun initVariables() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        navController = findNavController(R.id.nav_host_fragment)
    }

    internal fun addView(view: View, layoutParams: LayoutParams) {
        binding.coordinatorLayout.addView(view, layoutParams)
    }

    internal fun removeView(view: View) {
        binding.coordinatorLayout.removeView(view)
    }
}
