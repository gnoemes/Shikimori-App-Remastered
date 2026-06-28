package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.databinding.LayoutDetailsDescriptionBinding
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.utils.gone

class DetailsDescriptionViewHolder(
        private val binding: LayoutDetailsDescriptionBinding,
        navigationCallback: (Type, Long) -> Unit
) {

    private val placeholder by lazy { DetailsPlaceholderViewHolder(binding.descriptionContent.root, binding.descriptionPlaceholder.root) }

    init {
        binding.descriptionContent.descriptionTextView.linkCallback = navigationCallback
    }

    fun bind(item: DetailsDescriptionItem) {

        if (item.description.isNullOrEmpty()) {
            binding.root.gone()
            return
        }

        placeholder.showContent()

        binding.descriptionContent.descriptionTextView.setContent(item.description)
    }
}