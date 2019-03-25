package ru.wheelman.notes.presentation.customviews

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.appcompat.widget.LinearLayoutCompat
import org.jetbrains.anko.dip
import ru.wheelman.notes.model.entities.Note
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.datamappers.ColourMapper
import javax.inject.Inject

class ColorPickerView : LinearLayoutCompat {

    companion object {
        private const val PALETTE_ANIMATION_DURATION = 150L
        private const val HEIGHT = "height"
        private const val SCALE = "scale"
        @Dimension(unit = DP)
        private const val COLOR_VIEW_PADDING = 8
    }

    interface ColorClickListener {
        fun invoke(color: Note.Colour)
    }

    @Inject
    internal lateinit var colourMapper: ColourMapper
    var onColorClickListener: ColorClickListener? = null
    val isOpen: Boolean
        get() = measuredHeight > 0
    private var desiredHeight = 0

    private val animator by lazy {
        ValueAnimator().apply {
            duration = PALETTE_ANIMATION_DURATION
            addUpdateListener(updateListener)
        }
    }

    private val updateListener by lazy {
        ValueAnimator.AnimatorUpdateListener { animator ->
            layoutParams.apply {
                height = animator.getAnimatedValue(HEIGHT) as Int
                layoutParams = this
            }
            val scaleFactor = animator.getAnimatedValue(SCALE) as Float
            for (i in 0 until childCount) {
                getChildAt(i).apply {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = scaleFactor
                }
            }
        }
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initDagger()
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        Note.Colour.values().forEach { color ->
            addView(
                ColorCircleView(context).apply {
                    fillColorRes = colourMapper.colourTocolorId(color)
                    tag = color
                    dip(COLOR_VIEW_PADDING).let { setPadding(it, it, it, it) }
                    setOnClickListener { onColorClickListener?.invoke(it.tag as Note.Colour) }
                })
        }
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams.apply {
            desiredHeight = height
            height = 0
            layoutParams = this
        }
    }

    fun open() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, desiredHeight),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 1f)
        )
        animator.start()
    }

    fun close() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, 0),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 0f)
        )
        animator.start()
    }


}
