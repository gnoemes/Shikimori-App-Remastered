package com.gnoemes.shikimori.utils.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecorator(
        private val space: Int,
        private val firstAndLastDoubleSpacing: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        val lastIndex = parent.adapter?.itemCount?.minus(1)

        outRect.top = space
        if (pos == lastIndex) outRect.bottom = space

        if (firstAndLastDoubleSpacing && (pos == 0 || pos == lastIndex)) {
            if (pos == 0) outRect.top = space * 2
            else outRect.bottom = space * 2
        }

    }
}