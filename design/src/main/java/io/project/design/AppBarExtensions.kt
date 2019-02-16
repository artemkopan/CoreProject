package io.project.design

import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

inline fun AppBarLayout.canDragHandler(crossinline block: () -> Boolean) {
    post {
        ((layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior)
            .setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(p0: AppBarLayout): Boolean {
                    return block().also { io.project.common.Logger.d("can drag $it") }
                }
            })
    }
}
