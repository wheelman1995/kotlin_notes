package ru.wheelman.notes.di.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.auth.AuthFragment
import ru.wheelman.notes.presentation.auth.AuthFragmentViewModel
import ru.wheelman.notes.presentation.customviews.ColorPickerView
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
    fun inject(mainActivity: MainActivity)
    fun inject(authFragment: AuthFragment)
    fun inject(authFragmentViewModel: AuthFragmentViewModel)
    fun inject(colorPickerView: ColorPickerView)

    @Component.Builder
    interface Builder : AbstractBuilder<AppComponent> {

        @BindsInstance
        fun app(app: Application): Builder

    }
}