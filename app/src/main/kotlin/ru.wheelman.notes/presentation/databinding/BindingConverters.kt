package ru.wheelman.notes.presentation.databinding

import android.graphics.drawable.ColorDrawable
import androidx.databinding.BindingConversion
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.presentation.app.NotesApp

@BindingConversion
fun colourToColorDrawable(colour: Note.Colour) = ColorDrawable(
    NotesApp.appComponent.getColourMapper().colourToResource(colour)
)

@BindingConversion
fun colourToPlatformColor(colour: Note.Colour) =
    NotesApp.appComponent.getColourMapper().colourToResource(colour)
