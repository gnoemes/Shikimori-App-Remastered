package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.utils.gone
import kotlinx.android.synthetic.main.layout_details_description.view.*
import kotlinx.android.synthetic.main.layout_details_description_content.view.*

class DetailsDescriptionViewHolder(
        private val view: View,
        private val navigationCallback: (Type, Long) -> Unit
) {

    private val placeholder by lazy { DetailsPlaceholderViewHolder(view.descriptionContent, view.descriptionPlaceholder as ShimmerFrameLayout) }

    fun bind(item: DetailsDescriptionItem) {

        if (item.description.isNullOrEmpty()) {
            view.gone()
            return
        }

        placeholder.showContent()

        with(view) {
            descriptionTextView.linkCallback = navigationCallback
            descriptionTextView.setContent(item.description)
        }
    }


}