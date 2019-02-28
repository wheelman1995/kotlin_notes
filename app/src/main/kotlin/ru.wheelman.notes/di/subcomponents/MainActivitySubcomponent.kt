package ru.wheelman.notes.di.subcomponents

import dagger.BindsInstance
import dagger.Subcomponent
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.di.modules.MainActivityModule
import ru.wheelman.notes.di.scopes.MainActivityScope
import ru.wheelman.notes.view.MainActivity

@Subcomponent(modules = [MainActivityModule::class])
@MainActivityScope
interface MainActivitySubcomponent {
    fun inject(mainActivity: MainActivity)

    @Subcomponent.Builder
    interface Builder : AbstractBuilder<MainActivitySubcomponent> {
        @BindsInstance
        fun mainActivity(mainActivity: MainActivity): Builder

//        fun mainActivityModule(mainActivityModule: MainActivityModule) : Builder
    }
}