package ru.wheelman.notes.model.repositories

import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.data.INotesDataSource
import ru.wheelman.notes.model.entities.Note
import javax.inject.Inject

@AppScope
class NotesRepository @Inject constructor(private val notesDataSource: INotesDataSource) :
    INotesRepository {

    override fun getNotes(): List<Note> = notesDataSource.getNotes()
    override fun getNote(noteId: String) = notesDataSource.getNote(noteId)
    override fun saveOrReplace(note: Note) = notesDataSource.saveOrReplace(note)

}