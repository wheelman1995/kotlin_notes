package ru.wheelman.notes.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.ActivityMainBinding
import ru.wheelman.notes.di.subcomponents.MainActivitySubcomponent
import ru.wheelman.notes.viewmodel.IMainActivityViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModel: IMainActivityViewModel
    @Inject
    internal lateinit var navController: NavController
    internal lateinit var mainActivitySubcomponent: MainActivitySubcomponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initDagger()

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

    }

    private fun initDagger() {
        mainActivitySubcomponent =
            NotesApp.appComponent.mainActivitySubcomponent()
                .mainActivity(this)
                .build()
        mainActivitySubcomponent.inject(this)
    }
}
