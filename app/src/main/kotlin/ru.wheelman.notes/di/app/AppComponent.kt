package ru.wheelman.notes.di.app

import dagger.BindsInstance
import dagger.Component
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.main.MainFragment
import ru.wheelman.notes.presentation.main.MainFragmentViewModel
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragmentViewModel

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(noteEditorFragmentViewModel: NoteEditorFragmentViewModel)

    @Component.Builder
    interface Builder : AbstractBuilder<AppComponent> {

        @BindsInstance
        fun notesApp(notesApp: NotesApp): Builder

    }
}