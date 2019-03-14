package ru.wheelman.notes.model.datasources.local

import ru.wheelman.notes.model.entities.Note

interface INotesDataSource {

    fun getNotes(): List<Note>
    fun getNote(noteId: String): Note?
    fun saveOrReplace(note: Note)

}