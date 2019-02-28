package ru.wheelman.notes.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.ActivityMainBinding
import ru.wheelman.notes.viewmodel.IMainActivityViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModel: IMainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDagger()

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

    }

    private fun initDagger() {
        val mainActivitySubcomponent =
            NotesApp.appComponent.mainActivitySubcomponent()
                .mainActivity(this)
                .build()
        mainActivitySubcomponent.inject(this)
    }
}
