package io.project.recycler.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
class BottomSpaceItemDecoration(
    private var spacing: Int,
    private var isHorizontal: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == parent.adapter!!.itemCount - 1) {
            if (isHorizontal) {
                outRect.right = spacing
            } else {
                outRect.bottom = spacing
            }
        } else {
            outRect.setEmpty()
        }
    }
}