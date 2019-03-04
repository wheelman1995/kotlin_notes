package ru.wheelman.notes.viewmodel

import ru.wheelman.notes.model.entities.Note
import javax.inject.Inject

internal class NotesAdapterViewModel @Inject constructor() :
    INotesAdapterViewModelMutable {

    private var notes: List<Note> = listOf()
        set(value) {
            if (field != value) {
                field = value
                listener?.invoke()
            }
        }
    private var listener: (() -> Unit)? = null

    override fun unsubscribe() {
        listener = null
    }

    override fun subscribe(f: () -> Unit) {
        listener = f
    }

    override fun getTitle(position: Int) = notes[position].title

    override fun getBackgroundColor(position: Int) = notes[position].color

    override fun getBody(position: Int) = notes[position].body

    override fun getItemCount() = notes.size

    override fun setNewData(notes: List<Note>) {
        this.notes = notes
    }
}