package ru.wheelman.notes.model.entities

import ru.wheelman.notes.di.app.AppScope
import javax.inject.Inject

@AppScope
class CurrentUser @Inject constructor() {

    lateinit var id: String
}