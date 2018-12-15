package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_details_head_simple.view.*
import kotlinx.android.synthetic.main.layout_details_head_simple_content.view.*

class DetailsHeadSimpleViewHolder(
        private val view: View,
        private val imageLoader: ImageLoader
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(view.headContent, view.headPlaceholder as ShimmerFrameLayout) }

    fun bind(item: DetailsHeadSimpleItem) {
        placeholder.showContent()

        with(view) {
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
            otherView.text = item.othersLabel
            otherView.text = item.othersText

            jobView.visibleIf { !item.job.isNullOrBlank() }
            jobView.text = item.job
        }
    }
}