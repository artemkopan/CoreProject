package io.project.core.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.project.core.android.extensions.getStatusBarHeight

class StatusBarView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), context.getStatusBarHeight())
    }

}