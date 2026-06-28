package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.databinding.LayoutDetailsHeadSimpleBinding
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visibleIf

class DetailsHeadSimpleViewHolder(
        private val binding: LayoutDetailsHeadSimpleBinding,
        private val imageLoader: ImageLoader
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(binding.headContent.root, binding.headPlaceholder.root) }

    fun bind(item: DetailsHeadSimpleItem) {
        placeholder.showContent()

        with(binding.headContent) {
            imageLoader.setImageWithPlaceHolder(imageView, item.image.original)

            onRuLabelView.visibleIf { !item.firstName.isNullOrBlank() }
            onRuView.visibleIf { !item.firstName.isNullOrBlank() }
            onRuLabelView.text = item.firstLabel
            onRuView.text = item.firstName

            onJpLabelView.visibleIf { !item.secondName.isNullOrBlank() }
            onJpView.visibleIf { !item.secondName.isNullOrBlank() }
            onJpLabelView.text = item.secondLabel
            onJpView.text = item.secondName

            otherLabelView.visibleIf { !item.othersText.isNullOrBlank() }
            otherView.visibleIf { !item.othersText.isNullOrBlank() }
            otherLabelView.text = item.othersLabel
            otherView.text = item.othersText

            jobView.visibleIf { !item.job.isNullOrBlank() }
            jobView.text = item.job
        }
    }
}