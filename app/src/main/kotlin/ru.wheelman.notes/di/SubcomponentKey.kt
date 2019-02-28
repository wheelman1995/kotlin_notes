package ru.wheelman.notes.di

import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class SubcomponentKey(val value: KClass<out AbstractBuilder<out Any>>)