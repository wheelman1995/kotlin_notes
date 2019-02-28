package ru.wheelman.notes.di

interface AbstractBuilder<T> {
    fun build(): T
}