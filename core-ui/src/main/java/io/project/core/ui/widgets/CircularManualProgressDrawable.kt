package io.project.core.ui.widgets

import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Property
import android.view.animation.DecelerateInterpolator
import io.project.core.app.Delegates
import io.project.core.app.lazyNonSafety


@Suppress("JoinDeclarationAndAssignment")
class CircularManualProgressDrawable : Drawable() {

    /**
     * ProgressBar's line thickness
     */
    var strokeWidth = 4f
        set(value) {
            field = value
            foregroundPaint.strokeWidth = value
            backgroundPaint.strokeWidth = value
            invalidateSelf()
        }

    var progress by Delegates.observeChanges(0f) { invalidateSelf() }
    var max by Delegates.observeChanges(100f) { invalidateSelf() }

    var startAngle = -90f
        set(value) {
            field = value; invalidateSelf()
        }

    var backgroundColor = Color.WHITE
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidateSelf()
        }

    var foregroundColor = Color.DKGRAY
        set(value) {
            field = value
            foregroundPaint.color = foregroundColor
            invalidateSelf()
        }

    private val rectF: RectF = RectF()

    private val backgroundPaint: Paint
    private val foregroundPaint: Paint


    init {
        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint.color = foregroundColor
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        rectF.set(
            0 + strokeWidth / 2, 0 + strokeWidth / 2, bounds.right - strokeWidth / 2, bounds.bottom - strokeWidth / 2
        )
        rectF.left = bounds.left.toFloat() + strokeWidth / 2f + .5f
        rectF.right = bounds.right.toFloat() - strokeWidth / 2f - .5f
        rectF.top = bounds.top.toFloat() + strokeWidth / 2f + .5f
        rectF.bottom = bounds.bottom.toFloat() - strokeWidth / 2f - .5f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawOval(rectF, backgroundPaint)
        val angle = 360 * progress / max
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint)
    }

    override fun setAlpha(alpha: Int) {
        backgroundPaint.alpha = alpha
        foregroundPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    override fun setColorFilter(colorFilter: ColorFilter) {
        backgroundPaint.colorFilter = colorFilter
        foregroundPaint.colorFilter = colorFilter
    }

    fun setProgressWithAnimation(progress: Float) {
        progressAnimator.run {
            setFloatValues(progress)
            start()
        }
    }

    private val progressAnimator by lazyNonSafety {
        val objectAnimator = ObjectAnimator.ofFloat(this, PROGRESS, progress)
        objectAnimator.duration = 1500
        objectAnimator.interpolator = DecelerateInterpolator()
        return@lazyNonSafety objectAnimator
    }

    companion object {
        @JvmStatic
        private val PROGRESS =
            object : Property<CircularManualProgressDrawable, Float>(Float::class.java, "progress") {
                override fun get(target: CircularManualProgressDrawable?): Float = target?.progress ?: 0f
                override fun set(target: CircularManualProgressDrawable?, value: Float) {
                    target?.progress = value
                }
            }

    }

}