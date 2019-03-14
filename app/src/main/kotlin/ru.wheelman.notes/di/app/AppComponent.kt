package ru.wheelman.notes.di.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.presentation.main.MainFragment
import ru.wheelman.notes.presentation.main.MainFragmentViewModel
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragment
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragmentViewModel

@Component(modules = [AppModule::class, FirestoreModule::class])
@AppScope
interface AppComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(noteEditorFragmentViewModel: NoteEditorFragmentViewModel)
    fun inject(noteEditorFragment: NoteEditorFragment)

    @Component.Builder
    interface Builder : AbstractBuilder<AppComponent> {

        @BindsInstance
        fun app(app: Application): Builder

    }
}