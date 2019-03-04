package ru.wheelman.notes.di.modules

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.di.scopes.MainFragmentScope
import ru.wheelman.notes.view.fragments.MainFragment
import ru.wheelman.notes.view.fragments.NotesRvAdapter
import ru.wheelman.notes.viewmodel.IMainFragmentViewModel
import ru.wheelman.notes.viewmodel.MainFragmentViewModel

@Module
class MainFragmentModule {

    @Provides
    fun mainFragmentViewModel(mainFragment: MainFragment): IMainFragmentViewModel =
        ViewModelProviders.of(mainFragment).get(MainFragmentViewModel::class.java)

    @Provides
    @MainFragmentScope
    fun notesAdapter(mainFragmentViewModel: IMainFragmentViewModel, mainFragment: MainFragment) =
        NotesRvAdapter(mainFragmentViewModel.getNotesAdapterViewModel(), mainFragment)

}