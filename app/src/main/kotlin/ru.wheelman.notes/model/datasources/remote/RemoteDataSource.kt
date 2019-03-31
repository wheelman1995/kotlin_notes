package ru.wheelman.notes.model.datasources.remote

import kotlinx.coroutines.channels.ReceiveChannel
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result

interface RemoteDataSource {

    suspend fun getNoteById(noteId: String): Result
    suspend fun saveNote(note: Note): Result
    suspend fun subscribeToAllNotes(): ReceiveChannel<Result>
    suspend fun removeNote(noteId: String): Result
}
