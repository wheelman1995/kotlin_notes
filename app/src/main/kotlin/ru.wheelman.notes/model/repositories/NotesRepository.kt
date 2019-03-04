package ru.wheelman.notes.model.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.withContext
import ru.wheelman.notes.di.scopes.MainFragmentViewModelScope
import ru.wheelman.notes.model.data.INotesDataSource
import ru.wheelman.notes.model.entities.Note
import javax.inject.Inject

@MainFragmentViewModelScope
internal class NotesRepository @Inject constructor(private val notesDataSource: INotesDataSource) :
    INotesRepository {

    override suspend fun getNotes(channel: SendChannel<Int>): List<Note> {
        return withContext(Dispatchers.IO) {
            notesDataSource.getNotes(channel)
        }
    }
}