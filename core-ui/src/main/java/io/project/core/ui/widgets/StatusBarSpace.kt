package io.project.core.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import io.project.core.android.extensions.getStatusBarHeight

class StatusBarSpace @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), context.getStatusBarHeight())
        } else {
            setMeasuredDimension(0, 0)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {
        // do not draw
    }

}