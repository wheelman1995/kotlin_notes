package ru.wheelman.notes.di.components

import dagger.BindsInstance
import dagger.Component
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.di.modules.AppModule
import ru.wheelman.notes.di.modules.SubcomponentsModule
import ru.wheelman.notes.di.scopes.AppScope
import ru.wheelman.notes.di.subcomponents.MainActivitySubcomponent
import ru.wheelman.notes.di.subcomponents.MainFragmentSubcomponent

@Component(
    modules = [
        SubcomponentsModule::class,
        AppModule::class
    ]
)
@AppScope
interface AppComponent {
    fun mainActivitySubcomponent(): MainActivitySubcomponent.Builder
    fun mainFragmentSubcomponent(): MainFragmentSubcomponent.Builder

    @Component.Builder
    interface Builder : AbstractBuilder<AppComponent> {

        //        fun appModule(appModule: AppModule) : Builder
        @BindsInstance
        fun notesApp(notesApp: NotesApp): Builder
    }
}