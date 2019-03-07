package ru.wheelman.notes.presentation.datamappers

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ru.wheelman.notes.R
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.Note.Colour
import javax.inject.Inject

@AppScope
internal class ColourMapper @Inject constructor(private val context: Context) {

    private fun Int.resourceIdToColorInt() = ContextCompat.getColor(context, this)

    internal fun colourToResource(colour: Colour) = when (colour) {
        Colour.WHITE -> R.color.color_white.resourceIdToColorInt()
        Colour.VIOLET -> R.color.color_violet.resourceIdToColorInt()
        Colour.YELLOW -> R.color.color_yellow.resourceIdToColorInt()
        Colour.RED -> R.color.color_red.resourceIdToColorInt()
        Colour.PINK -> R.color.color_pink.resourceIdToColorInt()
        Colour.GREEN -> R.color.color_green.resourceIdToColorInt()
        Colour.BLUE -> R.color.color_blue.resourceIdToColorInt()
    }

    internal fun colorIdToColour(@ColorRes colour: Int) = when (colour) {
        R.color.color_white -> Colour.WHITE
        R.color.color_violet -> Colour.VIOLET
        R.color.color_yellow -> Colour.YELLOW
        R.color.color_red -> Colour.RED
        R.color.color_pink -> Colour.PINK
        R.color.color_green -> Colour.GREEN
        R.color.color_blue -> Colour.BLUE
        else -> null
    }

}