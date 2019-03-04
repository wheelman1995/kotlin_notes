package ru.wheelman.notes.di.subcomponents

import dagger.Subcomponent
import ru.wheelman.notes.di.AbstractBuilder
import ru.wheelman.notes.di.modules.MainFragmentViewModelModule
import ru.wheelman.notes.di.scopes.MainFragmentViewModelScope
import ru.wheelman.notes.viewmodel.MainFragmentViewModel

@Subcomponent(modules = [MainFragmentViewModelModule::class])
@MainFragmentViewModelScope
interface MainFragmentViewModelSubcomponent {

    fun inject(mainFragmentViewModel: MainFragmentViewModel)

    @Subcomponent.Builder
    interface Builder : AbstractBuilder<MainFragmentViewModelSubcomponent>
}