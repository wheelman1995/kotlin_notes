package ru.wheelman.notes.presentation.noteeditor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.abstraction.AbstractViewModel
import ru.wheelman.notes.presentation.app.NotesApp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteEditorFragmentViewModel(private val app: Application, noteId: String?) :
    AbstractViewModel(app) {

    @Inject
    internal lateinit var notesRepository: INotesRepository
    val title: ObservableField<String> = ObservableField()
    val body: ObservableField<String> = ObservableField()
    private val _label = MutableLiveData<String>()
    internal val label: LiveData<String> = _label
    private var note: Note = Note()
        set(value) {
            field = value
            mapNote(value)
        }

    private val sdf = SimpleDateFormat("yyyy/MMM/d HH:mm:ss", Locale.getDefault())

    init {
        initDagger()
        loadNote(noteId)
    }

    private fun loadNote(noteId: String?) {
        noteId?.let {
            viewModelScope.launch {
                val channel = notesRepository.getNoteById(noteId)
                val result = channel.receive()
                processResult(result)
            }
        }
    }

    override fun onSuccess(data: Any) {
        val note = data as Note
        this@NoteEditorFragmentViewModel.note = note
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun mapNote(note: Note) {
        title.set(note.title)
        body.set(note.body)
        _label.value = sdf.format(note.lastChanged)
    }

    private fun reverseMapNote(): Note {
        val title = title.get() ?: String()
        val body = body.get() ?: String()
        return note.copy(title = title, body = body)
    }

    private fun saveNote(note: Note) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
        }
    }

    fun afterTextChanged() {
        val newNote = reverseMapNote()
        if (newNote != note) {
            newNote.lastChanged = Date()
            saveNote(newNote)
            note = newNote
        }
    }

    class Factory(private val app: Application, private val noteId: String?) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            NoteEditorFragmentViewModel(app, noteId) as T
    }
}