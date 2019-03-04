package ru.wheelman.notes.model.data

import kotlinx.coroutines.channels.SendChannel
import ru.wheelman.notes.di.scopes.MainFragmentViewModelScope
import ru.wheelman.notes.model.entities.Note
import javax.inject.Inject

@MainFragmentViewModelScope
internal class NotesLocalDataSource @Inject constructor() : INotesDataSource {
    override suspend fun getNotes(channel: SendChannel<Int>): List<Note> {
        for (i in 1..100) {
            Thread.sleep(10L)
            channel.send(i)
        }
        channel.close()
        return notes
    }

    private val notes: List<Note> = listOf(
        Note(
            "Первая заметка",
            "Текст первой заметки. Не очень длинный, но интересный",
            0xfff06292.toInt()
        ),
        Note(
            "Вторая заметка",
            "Текст второй заметки. Не очень длинный, но интересный",
            0xff9575cd.toInt()
        ),
        Note(
            "Третья заметка",
            "Текст третьей заметки. Не очень длинный, но интересный",
            0xff64b5f6.toInt()
        ),
        Note(
            "Четвертая заметка",
            "Текст четвертой заметки. Не очень длинный, но интересный",
            0xff4db6ac.toInt()
        ),
        Note(
            "Пятая заметка",
            "Текст пятой заметки. Не очень длинный, но интересный",
            0xffb2ff59.toInt()
        ),
        Note(
            "Шестая заметка",
            "Текст шестой заметки. Не очень длинный, но интересный",
            0xffffeb3b.toInt()
        )
    )
}