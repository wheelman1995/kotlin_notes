package ru.wheelman.notes.presentation.utils

import android.content.Context
import ru.wheelman.notes.di.app.AppScope
import javax.inject.Inject
import kotlin.math.roundToInt

@AppScope
internal class DisplayMetricsHelper @Inject constructor(context: Context) {

    private val scalingFactor = context.resources.displayMetrics.density

    internal fun dpToPx(dp: Int) = (dp * scalingFactor).roundToInt()
    internal fun pxToDp(px: Int) = (px / scalingFactor).roundToInt()
}