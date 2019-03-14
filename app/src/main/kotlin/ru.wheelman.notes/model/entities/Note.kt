package ru.wheelman.notes.model.entities

import java.io.Serializable
import java.util.*

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = String(),
    val body: String = String(),
    val colour: Colour = Colour.WHITE
) : Serializable {

    var lastChanged: Date = Date()

    enum class Colour : Serializable {
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED,
        VIOLET,
        PINK
    }
}
