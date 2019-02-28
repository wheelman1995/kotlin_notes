package ru.wheelman.notes.di.modules

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.view.MainActivity
import ru.wheelman.notes.viewmodel.IMainActivityViewModel
import ru.wheelman.notes.viewmodel.MainActivityViewModel

@Module
class MainActivityModule {

    @Provides
    fun mainActivityViewModel(mainActivity: MainActivity): IMainActivityViewModel =
        ViewModelProviders.of(mainActivity).get(MainActivityViewModel::class.java)
}