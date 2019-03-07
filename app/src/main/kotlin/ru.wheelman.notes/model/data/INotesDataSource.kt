package ru.wheelman.notes.model.data

import ru.wheelman.notes.model.entities.Note

interface INotesDataSource {

    fun getNotes(): List<Note>
    fun getNote(noteId: String): Note?
    fun saveOrReplace(note: Note)

}