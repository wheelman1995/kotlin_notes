package ru.wheelman.notes.presentation.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.repositories.INotesRepository
import ru.wheelman.notes.presentation.abstraction.AbstractViewModel
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.datamappers.ColourMapper
import javax.inject.Inject

class MainFragmentViewModel(private val app: Application) : AbstractViewModel(app) {

    @Inject
    internal lateinit var notesRepository: INotesRepository
    @Inject
    internal lateinit var colourMapper: ColourMapper
    internal val notesAdapter: NotesAdapterViewModel = NotesAdapterViewModel()

    init {
        initDagger()
        subscribeToAllNotes()
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun subscribeToAllNotes() {
        viewModelScope.launch {
            val notesChannel = notesRepository.subscribeToAllNotes()
            while (true) {
                val result = notesChannel.receive()
                processResult(result)
            }
        }
    }

    override fun onSuccess(data: Any) {
        val notes = data as List<Note>
        notesAdapter.setNewData(notes)
    }

    override fun onCleared() {
        unsubscribeFromAllNotes()
        super.onCleared()
    }

    private fun unsubscribeFromAllNotes() {
        notesRepository.unsubscribeFromAllNotes()
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

        fun getBackgroundColor(position: Int): Int =
            colourMapper.colourToResource(notes[position].colour)

        fun getBody(position: Int) = notes[position].body
        internal fun getItemCount() = notes.size

        internal fun setNewData(notes: List<Note>) {
            this.notes = notes
        }
    }
}
