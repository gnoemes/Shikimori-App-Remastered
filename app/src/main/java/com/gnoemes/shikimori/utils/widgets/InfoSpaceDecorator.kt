package com.gnoemes.shikimori.utils.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.entity.common.presentation.InfoClickableItem
import com.gnoemes.shikimori.entity.common.presentation.InfoItem
import com.gnoemes.shikimori.presentation.view.common.adapter.InfoAdapter

class InfoSpaceDecorator(
        private val space: Int,
        private val firstClickableSpacing: Int,
        private val clickableSpacing: Int,
        private val firstItemSpacing: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val item = (parent.adapter as? InfoAdapter)?.items?.getOrNull(parent.getChildAdapterPosition(view))

        if (item is InfoClickableItem) {
            if ((parent.adapter as? InfoAdapter)?.items?.getOrNull(parent.getChildAdapterPosition(view) - 1) is InfoItem)
                outRect.left = firstClickableSpacing
            else outRect.left = clickableSpacing
        } else {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = space
            } else {
                outRect.left = firstItemSpacing
            }
        }

        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1) && item !is InfoItem) {
            outRect.right = firstItemSpacing
        }
    }
}