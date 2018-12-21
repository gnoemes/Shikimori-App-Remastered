package com.gnoemes.shikimori.presentation.view.common.holders

import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.layout_details_description.view.*
import kotlinx.android.synthetic.main.layout_details_description_content.view.*

class DetailsDescriptionViewHolder(
        private val view: View,
        private val navigationCallback: (Type, Long) -> Unit
) {

    private val placeholder by lazy { DetailsPlaceholderViewHolder(view.descriptionContent, view.descriptionPlaceholder as ShimmerFrameLayout) }

    private val COLLAPSED_MAX_HEIGHT = (view.resources.displayMetrics.density * 80).toInt()
    private var contentHeight: Int = COLLAPSED_MAX_HEIGHT

    var isExpanded: Boolean = false

    fun bind(item: DetailsDescriptionItem) {

        if (item.description.isNullOrEmpty()) {
            view.gone()
            return
        }

        placeholder.showContent()

        with(view) {
            descriptionTextView.linkCallback = navigationCallback
            descriptionTextView.setContent(item.description)

            descriptionTextView.post {
                contentHeight = descriptionTextView.height
                if (descriptionTextView.height >= COLLAPSED_MAX_HEIGHT) {
                    descriptionTextView.layoutParams.height = COLLAPSED_MAX_HEIGHT
                    descriptionTextView.requestLayout()
                    expandView.visible()
                    expandView.onClick { expandOrCollapse() }
                } else expandView.gone()
            }
        }
    }

    private fun expandOrCollapse() {
        if (view.descriptionTextView.height >= COLLAPSED_MAX_HEIGHT) {
            isExpanded = !isExpanded
            if (isExpanded) view.expandView.setImageResource(R.drawable.ic_chevron_up)
            else view.expandView.setImageResource(R.drawable.ic_chevron_down)

            cycleHeightExpansion(view.descriptionTextView)
        }
    }

    private fun cycleHeightExpansion(layout: LinearLayout) {
        val end = if (layout.height == COLLAPSED_MAX_HEIGHT) contentHeight else COLLAPSED_MAX_HEIGHT

        ValueAnimator.ofInt(layout.height, end)
                .apply { addUpdateListener { layout.layoutParams.apply { height = it.animatedValue as Int; layout.requestLayout() } } }
                .setDuration(500)
                .start()
    }

}