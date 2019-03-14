package ru.wheelman.notes.model.entities

sealed class Result {
    data class Success<T : Any>(val data: T) : Result()
    data class Error(val error: Throwable? = null) : Result()
}