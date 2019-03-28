package io.project.core.android.pager

import android.view.View
import androidx.viewpager.widget.ViewPager


class FadePageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        applyTransform(view, view.width, position)
    }

    fun applyTransform(view: View, width: Int, position: Float) {
        view.translationX = width * -position

        if (position <= -1.0F || position >= 1.0F) {
            view.alpha = 0.0F
        } else if (position == 0.0F) {
            view.alpha = 1.0F
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.alpha = 1.0F - Math.abs(position)
        }
    }
}
