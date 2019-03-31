package ru.wheelman.notes.model.repositories

import kotlinx.coroutines.channels.ReceiveChannel
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.datasources.remote.RemoteDataSource
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result
import javax.inject.Inject

@AppScope
class NotesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    INotesRepository {

    override suspend fun getNoteById(noteId: String): Result =
        remoteDataSource.getNoteById(noteId)

    override suspend fun saveNote(note: Note): Result =
        remoteDataSource.saveNote(note)

    override suspend fun subscribeToAllNotes(): ReceiveChannel<Result> =
        remoteDataSource.subscribeToAllNotes()

    override suspend fun removeNote(noteId: String): Result =
        remoteDataSource.removeNote(noteId)
}