package io.project.core.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import io.project.core.ui.R

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val drawable = CircularManualProgressDrawable()

    init {

        if (attrs != null) {
            val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressView, 0, 0)

            ta.getDimensionPixelSize(R.styleable.CircularProgressView_cpv_strokeWidth, drawable.strokeWidth.toInt())
                .run {
                    strokeWidth = this.toFloat()
                }

            ta.getFloat(R.styleable.CircularProgressView_cpv_maxProgress, drawable.max).run {
                maxProgress = this
            }

            ta.getFloat(R.styleable.CircularProgressView_cpv_startAngle, drawable.startAngle).run {
                startAngle = this
            }

            ta.getColor(R.styleable.CircularProgressView_cpv_backgroundColor, drawable.backgroundColor)
                .run {
                    progressBackgroundColor = this
                }

            ta.getColor(R.styleable.CircularProgressView_cpv_foregroundColor, drawable.foregroundColor)
                .run {
                    progressForegroundColor = this
                }

        }
        drawable.callback = this
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawable.setBounds(0, 0, w, h)
    }

    override fun onDraw(canvas: Canvas) {
        drawable.draw(canvas)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === drawable || super.verifyDrawable(who)
    }

    var strokeWidth
        set(value) {
            drawable.strokeWidth = value
        }
        get() = drawable.strokeWidth

    var progress
        set(value) {
            drawable.progress = value
        }
        get() = drawable.progress

    var maxProgress
        set(value) {
            drawable.max = value
        }
        get() = drawable.max

    var startAngle
        set(value) {
            drawable.startAngle = value
        }
        get() = drawable.startAngle

    var progressBackgroundColor
        set(value) {
            drawable.backgroundColor = value
        }
        get() = drawable.backgroundColor

    var progressForegroundColor
        set(value) {
            drawable.foregroundColor = value
        }
        get() = drawable.foregroundColor


    fun setProgressAnimate(progress: Float) = drawable.setProgressWithAnimation(progress)


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(ARG_SUPER_STATE, super.onSaveInstanceState())
        bundle.putFloat(ARG_PROGRESS, maxProgress)
        bundle.putFloat(PROGRESS, progress)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (state is Bundle) {
            maxProgress = state.getFloat(ARG_PROGRESS, maxProgress)
            progress = state.getFloat(PROGRESS, progress)
            newState = state.getParcelable(ARG_SUPER_STATE)
        }
        super.onRestoreInstanceState(newState)
    }

    companion object {
        private const val ARG_SUPER_STATE = "superState"
        private const val ARG_PROGRESS = "maxProgress"
        private const val PROGRESS = "progress"
    }
}
