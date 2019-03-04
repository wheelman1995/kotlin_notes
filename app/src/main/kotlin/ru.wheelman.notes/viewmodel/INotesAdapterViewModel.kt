package ru.wheelman.notes.viewmodel

import ru.wheelman.notes.model.entities.Note

interface INotesAdapterViewModelMutable : INotesAdapterViewModel {
    fun setNewData(notes: List<Note>)
}

interface INotesAdapterViewModel {
    fun getTitle(position: Int): String
    fun getBody(position: Int): String
    fun getItemCount(): Int
    fun unsubscribe()
    fun subscribe(f: () -> Unit)
    fun getBackgroundColor(position: Int): Int
}