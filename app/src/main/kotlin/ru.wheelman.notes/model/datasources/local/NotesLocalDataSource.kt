package ru.wheelman.notes.model.datasources.local

import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.model.entities.Note.Colour
import javax.inject.Inject

@AppScope
class NotesLocalDataSource @Inject constructor() :
    INotesDataSource {

    override fun getNotes(): List<Note> = notes
    override fun getNote(noteId: String) = notes.firstOrNull { it.id == noteId }

    override fun saveOrReplace(note: Note) {
        for ((index, value) in notes.withIndex()) {
            if (value.id == note.id) {
                notes[index] = note
                return
            }
        }
        notes.add(note)
    }

    private val notes: MutableList<Note> = mutableListOf(
        Note(
            title = "Первая заметка",
            body = "Текст первой заметки. Не очень длинный, но интересный",
            colour = Colour.WHITE
        ),
        Note(
            title = "Вторая заметка",
            body = "Текст второй заметки. Не очень длинный, но интересный",
            colour = Colour.YELLOW
        ),
        Note(
            title = "Третья заметка",
            body = "Текст третьей заметки. Не очень длинный, но интересный",
            colour = Colour.GREEN
        ),
        Note(
            title = "Четвертая заметка",
            body = "Текст четвертой заметки. Не очень длинный, но интересный",
            colour = Colour.BLUE
        ),
        Note(
            title = "Пятая заметка",
            body = "Текст пятой заметки. Не очень длинный, но интересный",
            colour = Colour.RED
        ),
        Note(
            title = "Шестая заметка",
            body = "Текст шестой заметки. Не очень длинный, но интересный",
            colour = Colour.VIOLET
        )
    )
}