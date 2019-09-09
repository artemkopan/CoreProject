package io.project.core.android.extensions

import android.view.View


private var lastClickTimestamp = 0L

/**
 * @param delay - by default  500 ms;
 */
fun View.setThrottledClickListener(delay: Long = 500L, clickListener: View.OnClickListener) {
    setOnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val delta = currentTimeStamp - lastClickTimestamp
        if (delta !in 0L..delay) {
            lastClickTimestamp = currentTimeStamp
            clickListener.onClick(this)
        }
    }
}
