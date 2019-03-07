package ru.wheelman.notes.di.app

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.di.app.AppModule.Binder
import ru.wheelman.notes.model.data.INotesDataSource
import ru.wheelman.notes.model.data.NotesLocalDataSource
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.model.repositories.NotesRepository
import ru.wheelman.notes.presentation.app.NotesApp

@Module(includes = [Binder::class])
class AppModule {

    @Provides
    @AppScope
    fun appContext(notesApp: NotesApp): Context = notesApp

    @Module
    interface Binder {

        @Binds
        fun notesDataSource(notesLocalDataSource: NotesLocalDataSource): INotesDataSource

        @Binds
        fun notesRepository(notesRepository: NotesRepository): INotesRepository

    }
}