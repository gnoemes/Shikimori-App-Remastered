package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.isGone
import com.gnoemes.shikimori.utils.isVisible
import com.gnoemes.shikimori.utils.visible

class DetailsPlaceholderViewHolder(
        private val content : View,
        private val placeholder : ShimmerFrameLayout
) {

    fun showContent() {
        if (placeholder.isVisible()) {
            placeholder.stopShimmer()
            placeholder.gone()
            content.visible()
        }
    }

    fun hideContent() {
        if (placeholder.isGone()) {
            placeholder.startShimmer()
            placeholder.visible()
            content.gone()
        }
    }
}