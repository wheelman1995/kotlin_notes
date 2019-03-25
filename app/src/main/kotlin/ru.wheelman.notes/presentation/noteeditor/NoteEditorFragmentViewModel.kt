package ru.wheelman.notes.presentation.noteeditor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.abstraction.AbstractViewModel
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.app.logd
import ru.wheelman.notes.presentation.datamappers.ColourMapper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteEditorFragmentViewModel(private val app: Application, noteId: String?) :
    AbstractViewModel(app) {

    @Inject
    internal lateinit var notesRepository: INotesRepository
    @Inject
    internal lateinit var colourMapper: ColourMapper
    val title: ObservableField<String> = ObservableField()
    val body: ObservableField<String> = ObservableField()
    val backgroundColor = ObservableInt()
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
                processResult(result) {
                    val note = it as Note
                    this@NoteEditorFragmentViewModel.note = note
                }
            }
        }
    }

    fun onColorViewClick(colour: Note.Colour) {
        logd("onColorViewClick")
        if (note.colour != colour) {
            note = note.copy(colour = colour)
            saveNote()
        }
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun mapNote(note: Note) {
        title.set(note.title)
        body.set(note.body)
        _label.value = sdf.format(note.lastChanged)
        backgroundColor.set(colourMapper.colourToResource(note.colour))
    }

    private fun reverseMapNote(): Note {
        val title = title.get() ?: String()
        val body = body.get() ?: String()
        return note.copy(title = title, body = body)
    }

    private fun saveNote() {
        viewModelScope.launch {
            note.lastChanged = Date()
            notesRepository.saveNote(note)
        }
    }

    fun afterTextChanged() {
        val newNote = reverseMapNote()
        if (newNote != note) {
            note = newNote
            saveNote()
        }
    }

    fun deleteNote(onSuccess: () -> Unit) {
        logd("deleteNote")
        viewModelScope.launch {
            val channel = notesRepository.removeNote(note.id)
            val result = channel.receive()
            processResult(result) { onSuccess() }
        }
    }

    class Factory(private val app: Application, private val noteId: String?) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            NoteEditorFragmentViewModel(app, noteId) as T
    }
}