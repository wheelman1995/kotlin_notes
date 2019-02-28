package ru.wheelman.notes.di.modules

import dagger.Module
import ru.wheelman.notes.di.subcomponents.MainActivitySubcomponent
import ru.wheelman.notes.di.subcomponents.MainFragmentSubcomponent

@Module(
    subcomponents = [
        MainActivitySubcomponent::class,
        MainFragmentSubcomponent::class
    ]
)
class SubcomponentsModule {

}