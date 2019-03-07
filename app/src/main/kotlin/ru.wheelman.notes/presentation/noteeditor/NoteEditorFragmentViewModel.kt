package ru.wheelman.notes.presentation.noteeditor

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.app.NotesApp
import java.util.*
import javax.inject.Inject

class NoteEditorFragmentViewModel(noteId: String?) : ViewModel() {

    @Inject
    internal lateinit var notesRepository: INotesRepository
    val title: ObservableField<String> = ObservableField()
    val body: ObservableField<String> = ObservableField()
    private lateinit var note: Note

    init {
        initDagger()
        loadNote(noteId)
    }

    private fun loadNote(noteId: String?) {
        noteId?.let {
            notesRepository.getNote(noteId)?.let {
                this.note = it
                mapNote(it)
                return
            }
        }
        note = Note()
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun mapNote(note: Note) {
        title.set(note.title)
        body.set(note.body)
    }

    private fun reverseMapNote(): Note {
        val title = title.get() ?: String()
        val body = body.get() ?: String()
        return note.copy(title = title, body = body)
    }

    private fun saveNote(note: Note) {
        note.lastChanged = Date()
        notesRepository.saveOrReplace(note)
    }

    fun afterTextChanged() {
        val newNote = reverseMapNote()
        if (newNote != note) {
            saveNote(newNote)
            note = newNote
        }
    }

    class Factory(private val noteId: String?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            NoteEditorFragmentViewModel(noteId) as T
    }
}