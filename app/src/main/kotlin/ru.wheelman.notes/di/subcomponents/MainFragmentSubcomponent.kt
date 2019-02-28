package ru.wheelman.notes.di.subcomponents

import dagger.BindsInstance
import dagger.Subcomponent
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.di.modules.MainFragmentModule
import ru.wheelman.notes.di.scopes.MainFragmentScope
import ru.wheelman.notes.view.fragments.MainFragment

@Subcomponent(modules = [MainFragmentModule::class])
@MainFragmentScope
interface MainFragmentSubcomponent {
    fun inject(mainFragment: MainFragment)

    @Subcomponent.Builder
    interface Builder : AbstractBuilder<MainFragmentSubcomponent> {

//        fun mainFragmentModule(mainFragmentModule: MainFragmentModule) : Builder

        @BindsInstance
        fun mainFragment(mainFragment: MainFragment): Builder
    }
}