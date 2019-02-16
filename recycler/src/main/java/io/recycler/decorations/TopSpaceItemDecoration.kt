package io.recycler.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
class TopSpaceItemDecoration(
    private var spacing: Int,
    private var isHorizontal: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            if (isHorizontal) {
                outRect.left = spacing
            } else {
                outRect.top = spacing
            }
        }
    }
}