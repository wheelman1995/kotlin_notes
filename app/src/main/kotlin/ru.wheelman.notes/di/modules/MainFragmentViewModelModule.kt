package ru.wheelman.notes.di.modules

import dagger.Binds
import dagger.Module
import ru.wheelman.notes.di.modules.MainFragmentViewModelModule.Binder
import ru.wheelman.notes.model.data.INotesDataSource
import ru.wheelman.notes.model.data.NotesLocalDataSource
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.model.repositories.NotesRepository
import ru.wheelman.notes.viewmodel.INotesAdapterViewModelMutable
import ru.wheelman.notes.viewmodel.NotesAdapterViewModel

@Module(includes = [Binder::class])
internal class MainFragmentViewModelModule {

    @Module
    interface Binder {
        @Binds
        fun notesAdapterViewModel(notesAdapterViewModel: NotesAdapterViewModel): INotesAdapterViewModelMutable

        @Binds
        fun notesDataSource(notesLocalDataSource: NotesLocalDataSource): INotesDataSource

        @Binds
        fun notesRepository(notesRepository: NotesRepository): INotesRepository
    }
}