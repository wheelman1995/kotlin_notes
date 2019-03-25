package ru.wheelman.notes.model.datasources.remote

import kotlinx.coroutines.channels.ReceiveChannel
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result

interface RemoteDataSource {

    suspend fun getNoteById(noteId: String): ReceiveChannel<Result>
    suspend fun saveNote(note: Note): ReceiveChannel<Result>
    suspend fun subscribeToAllNotes(): ReceiveChannel<Result>
    fun unsubscribeFromAllNotes()
    suspend fun removeNote(noteId: String): ReceiveChannel<Result>
}
