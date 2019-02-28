package ru.wheelman.notes.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.di.scopes.AppScope

@Module
class AppModule {

    @Provides
    @AppScope
    fun appContext(notesApp: NotesApp): Context = notesApp


}