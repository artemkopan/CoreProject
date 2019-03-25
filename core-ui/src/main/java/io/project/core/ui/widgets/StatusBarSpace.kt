package io.project.core.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import io.project.core.android.extensions.getStatusBarHeight

class StatusBarSpace @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), context.getStatusBarHeight())
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {
        // do not draw
    }

}