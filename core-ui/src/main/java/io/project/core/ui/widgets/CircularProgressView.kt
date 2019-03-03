package io.project.core.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
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
                    drawable.strokeWidth = this.toFloat()
                }

            ta.getFloat(R.styleable.CircularProgressView_cpv_maxProgress, drawable.max).run {
                drawable.max = this
            }

            ta.getFloat(R.styleable.CircularProgressView_cpv_startAngle, drawable.startAngle).run {
                drawable.startAngle = this
            }

            ta.getColor(R.styleable.CircularProgressView_cpv_backgroundColor, drawable.backgroundColor)
                .run {
                    drawable.backgroundColor = this
                }

            ta.getColor(R.styleable.CircularProgressView_cpv_foregroundColor, drawable.foregroundColor)
                .run {
                    drawable.foregroundColor = this
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

}
