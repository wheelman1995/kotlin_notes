package ru.wheelman.notes.presentation.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Result
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.abstraction.AbstractViewModel
import ru.wheelman.notes.presentation.app.NotesApp
import javax.inject.Inject

class MainFragmentViewModel(private val app: Application) : AbstractViewModel(app) {

    init {
        initDagger()
        subscribeToAllNotes()
    }

    @Inject
    internal lateinit var notesRepository: INotesRepository
    internal val notesAdapter: NotesAdapterViewModel = NotesAdapterViewModel()
    private lateinit var notesChannel: ReceiveChannel<Result>

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun subscribeToAllNotes() {
        viewModelScope.launch {
            notesChannel = notesRepository.subscribeToAllNotes()
            notesChannel.consumeEach { result ->
                processResult(result) {
                    val notes = it as List<Note>
                    notesAdapter.setNewData(notes)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }

    inner class NotesAdapterViewModel {

        private var notes: List<Note> = listOf()
            set(value) {
                if (field != value) {
                    field = value
                    listener?.invoke()
                }
            }

        private var listener: (() -> Unit)? = null

        internal fun unsubscribe() {
            listener = null
        }

        internal fun subscribe(f: () -> Unit) {
            listener = f
        }

        fun getNoteIdForPosition(position: Int) = notes[position].id
        fun getTitle(position: Int) = notes[position].title

        fun getBackgroundColor(position: Int) = notes[position].colour

        fun getBody(position: Int) = notes[position].body
        internal fun getItemCount() = notes.size

        internal fun setNewData(notes: List<Note>) {
            this.notes = notes
        }
    }
}
