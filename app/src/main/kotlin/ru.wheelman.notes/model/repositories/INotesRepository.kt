package ru.wheelman.notes.model.repositories

import ru.wheelman.notes.model.entities.Note

interface INotesRepository {

    fun getNotes(): List<Note>
    fun getNote(noteId: String): Note?
    fun saveOrReplace(note: Note)

}