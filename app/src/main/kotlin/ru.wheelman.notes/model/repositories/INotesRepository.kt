package ru.wheelman.notes.model.repositories

import kotlinx.coroutines.channels.SendChannel
import ru.wheelman.notes.model.entities.Note

interface INotesRepository {
    suspend fun getNotes(channel: SendChannel<Int>): List<Note>
}