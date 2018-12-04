package com.gnoemes.shikimori.utils.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecorator(
        private val space: Int,
        private val firstItemSpacing : Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = space
        } else {
            outRect.left = firstItemSpacing
        }

        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
            outRect.right = space
        }
    }
}