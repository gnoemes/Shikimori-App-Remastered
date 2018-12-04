package com.gnoemes.shikimori.presentation.view.common.holders

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.layout_details_description.view.*

class DetailsDescriptionViewHolder(
        private val view: View
) {

    private companion object {
        private const val COLLAPSED_MAX_LINES = 4
    }

    var isExpanded: Boolean = false

    fun bind(item: DetailsDescriptionItem) {

        if (item.description.isNullOrEmpty()) {
            view.gone()
            return
        }

        with(view) {
            descriptionTextView.text = item.description

            if (descriptionTextView.lineCount > COLLAPSED_MAX_LINES) {
                descriptionTextView.post { expandView.visible() }
            }

            descriptionTextView.onClick { expandOrCollapse() }
            expandView.onClick { expandOrCollapse() }
        }
    }

    private fun expandOrCollapse() {
        if (view.descriptionTextView.lineCount > COLLAPSED_MAX_LINES) {
            isExpanded = !isExpanded
            if (isExpanded) view.expandView.setImageResource(R.drawable.ic_chevron_up)
            else view.expandView.setImageResource(R.drawable.ic_chevron_down)

            cycleTextViewExpansion(view.descriptionTextView)
        }
    }

    private fun cycleTextViewExpansion(tv: TextView) {
        val duration = (tv.lineCount - COLLAPSED_MAX_LINES) * 10L
        val animation = ObjectAnimator.ofInt(tv, "maxLines",
                if (tv.maxLines == COLLAPSED_MAX_LINES) tv.lineCount else COLLAPSED_MAX_LINES)
        animation.setDuration(duration).start()
    }
}