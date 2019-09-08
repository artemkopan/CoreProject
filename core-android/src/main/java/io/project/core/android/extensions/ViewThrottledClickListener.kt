package io.project.core.android.extensions

import android.view.View


private var lasClickTimestamp = 0L

/**
 * Default delay 500 ms;
 */
fun View.setThrottledClickListener(delay: Long = 500L, clickListener: (View) -> Unit) {
    setOnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val delta = currentTimeStamp - lasClickTimestamp
        if (delta !in 0L..delta) {
            lasClickTimestamp = currentTimeStamp
            clickListener(this)
        }
    }
}
