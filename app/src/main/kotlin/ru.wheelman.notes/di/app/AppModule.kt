package ru.wheelman.notes.di.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import ru.wheelman.notes.di.app.AppModule.Binder
import ru.wheelman.notes.model.datasources.remote.FirestoreDataSource
import ru.wheelman.notes.model.datasources.remote.RemoteDataSource
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.model.repositories.NotesRepository

@Module(includes = [Binder::class])
class AppModule {

    @Module
    interface Binder {

        @Binds
        @AppScope
        fun notesRepository(notesRepository: NotesRepository): INotesRepository

        @Binds
        @AppScope
        fun notesRemoteDataSource(firestoreDataSource: FirestoreDataSource): RemoteDataSource

        @Binds
        @AppScope
        fun appContext(app: Application): Context
    }
}