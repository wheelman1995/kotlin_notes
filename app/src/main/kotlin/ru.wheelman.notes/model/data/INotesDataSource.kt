package ru.wheelman.notes.model.data

import kotlinx.coroutines.channels.SendChannel
import ru.wheelman.notes.model.entities.Note

interface INotesDataSource {
    suspend fun getNotes(channel: SendChannel<Int>): List<Note>
}