package ru.wheelman.notes.di.modules

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.R
import ru.wheelman.notes.di.scopes.MainActivityScope
import ru.wheelman.notes.di.subcomponents.MainFragmentSubcomponent
import ru.wheelman.notes.view.MainActivity
import ru.wheelman.notes.viewmodel.IMainActivityViewModel
import ru.wheelman.notes.viewmodel.MainActivityViewModel

@Module(subcomponents = [MainFragmentSubcomponent::class])
class MainActivityModule {

    @Provides
    @MainActivityScope
    fun navController(mainActivity: MainActivity) =
        mainActivity.findNavController(R.id.nav_host_fragment)

    @Provides
    fun mainActivityViewModel(mainActivity: MainActivity): IMainActivityViewModel =
        ViewModelProviders.of(mainActivity).get(MainActivityViewModel::class.java)
}