package com.gnoemes.shikimori.utils.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecorator(
        private val space: Int,
        private val firstAndLastDoubleOrCustomSpacing: Boolean = false,
        private val firstCustomSpacing: Int = -1,
        private val lastCustomSpacing: Int = -1
) : RecyclerView.ItemDecoration() {

    private val firstHasCustom: Boolean
        get() = firstCustomSpacing != -1

    private val lastHasCustom: Boolean
        get() = lastCustomSpacing != -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        val lastIndex = parent.adapter?.itemCount?.minus(1)

        outRect.top = space
        if (pos == lastIndex) outRect.bottom = space

        if (firstAndLastDoubleOrCustomSpacing && (pos == 0 || pos == lastIndex)) {
            if (pos == 0) outRect.top = if (!firstHasCustom) space * 2 else firstCustomSpacing
            else outRect.bottom = if (!lastHasCustom) space * 2 else lastCustomSpacing
        }
    }
}