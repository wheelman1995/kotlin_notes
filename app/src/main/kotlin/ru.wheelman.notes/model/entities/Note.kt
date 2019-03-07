package ru.wheelman.notes.model.entities

import java.util.*

data class Note(
    internal val id: String = UUID.randomUUID().toString(),
    internal val title: String = String(),
    internal val body: String = String(),
    internal val colour: Colour = Colour.WHITE
) {

    internal var lastChanged: Date = Date()

    enum class Colour {
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED,
        VIOLET,
        PINK
    }
}
