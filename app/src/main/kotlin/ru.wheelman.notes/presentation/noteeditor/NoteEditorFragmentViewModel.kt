package ru.wheelman.notes.presentation.noteeditor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.abstraction.AbstractViewModel
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.app.logd
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteEditorFragmentViewModel(private val app: Application, noteId: String?) :
    AbstractViewModel(app), LifecycleObserver {

    @Inject
    internal lateinit var notesRepository: INotesRepository
    val title: ObservableField<String> = ObservableField()
    val body: ObservableField<String> = ObservableField()
    private val _label = MutableLiveData<String>()
    internal val label: LiveData<String> = _label
    private var note: Note? = Note()
        set(value) {
            field = value
            value?.let { mapNote(value) }
        }
    val backgroundColor = ObservableField<Note.Colour>(note!!.colour)
    private val sdf = SimpleDateFormat("yyyy/MMM/d HH:mm:ss", Locale.getDefault())

    init {
        initDagger()
        loadNote(noteId)
    }

    private fun loadNote(noteId: String?) {
        noteId?.let {
            viewModelScope.launch {
                processResult(notesRepository.getNoteById(noteId)) {
                    val note = it as Note
                    this@NoteEditorFragmentViewModel.note = note
                }
            }
        }
    }

    fun onColorViewClick(colour: Note.Colour) {
        backgroundColor.set(colour)
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun mapNote(note: Note) {
        title.set(note.title)
        body.set(note.body)
        _label.value = sdf.format(note.lastChanged)
        backgroundColor.set(note.colour)
    }

    private fun reverseMapNote(): Note? {
        val title = title.get() ?: String()
        val body = body.get() ?: String()
        val colour = backgroundColor.get() ?: Note.Colour.WHITE
        return note?.copy(title = title, body = body, colour = colour)
    }

    @OnLifecycleEvent(ON_STOP)
    fun saveNote() {
        logd("saveNote")
        val newNote = reverseMapNote()
        newNote?.let {
            if (newNote != note) {
                newNote.lastChanged = Date()
                note = newNote
                GlobalScope.launch {
                    notesRepository.saveNote(newNote)
                }
            }
        }
    }

    fun deleteNote(onSuccess: () -> Unit) {
        val localNote = note
        localNote?.let {
            viewModelScope.launch {
                processResult(notesRepository.removeNote(localNote.id)) {
                    note = null
                    onSuccess()
                }
            }
        }
    }

    class Factory(private val app: Application, private val noteId: String?) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            NoteEditorFragmentViewModel(app, noteId) as T
    }
}