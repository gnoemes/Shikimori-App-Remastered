package com.gnoemes.shikimori.presentation.view.common.holders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutDetailsHeadBinding
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.utils.images.ImageLoader

class DetailsHeadViewHolder(
        private val binding: LayoutDetailsHeadBinding,
        private val imageLoader: ImageLoader,
        private val callback: (DetailsAction) -> Unit
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(binding.headContent.root, binding.headPlaceholder.root) }

    init {
        with(binding.headContent) {
            editRateBtn.onClick { callback.invoke(DetailsAction.EditRate) }
            statusRateBtn.onClick { callback.invoke(DetailsAction.RateStatusDialog) }

            val context = binding.root.context
            val emptyDrawable = ContextCompat.getDrawable(context, R.drawable.ic_small_star_empty) ?: return@with
            val filledDrawable = ContextCompat.getDrawable(context, R.drawable.ic_small_star_filled) ?: return@with
            ratingView.setEmptyDrawable(emptyDrawable.apply { tint(context.colorAttr(R.attr.colorOnPrimarySecondary)) })
            ratingView.setFilledDrawable(filledDrawable.apply { tint(context.colorAttr(R.attr.colorSecondary)) })
        }
    }

    fun bind(item: DetailsHeadItem) {
        placeholder.showContent()

        with(binding.headContent) {
            if (!imageView.hasImage()) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)
            }

            nameView.text = item.name

            ratingView.rating = (item.score / 2).toFloat()
            ratingValueView.text = item.score.toString()


            editRateBtn.visibleIf { !item.isGuest }
            statusRateBtn.visibleIf { !item.isGuest }

            if (item.rateStatus != null) setStatus(item.rateStatus)
            else {
                with(statusRateBtn) {
                    setIconResource(R.drawable.ic_plus)
                    iconTint = ContextCompat.getColorStateList(binding.root.context, R.color.selector_circle_button_text_color)
                    backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.selector_gray_circle_background_color)
                    rippleColor = ContextCompat.getColorStateList(binding.root.context, R.color.selector_button_ripple_color)
                }
            }
        }
    }

    private fun setStatus(rateStatus: RateStatus) {
        val iconAndColors = when (rateStatus) {
            RateStatus.PLANNED -> Triple(R.drawable.ic_planned, R.color.rate_default_transparent, R.color.rate_default_dark)
            RateStatus.WATCHING -> Triple(R.drawable.ic_play_rate, R.color.rate_default_transparent, R.color.rate_default_dark)
            RateStatus.REWATCHING -> Triple(R.drawable.ic_replay, R.color.rate_default_transparent, R.color.rate_default_dark)
            RateStatus.DROPPED -> Triple(R.drawable.ic_close, R.color.rate_dropped_transparent, R.color.rate_dropped_dark)
            RateStatus.ON_HOLD -> Triple(R.drawable.ic_pause_rate, R.color.rate_on_hold_transparent, R.color.rate_on_hold_dark)
            RateStatus.COMPLETED -> Triple(R.drawable.ic_check, R.color.rate_watched_transparent, R.color.rate_watched_dark)
        }
        with(binding.headContent.statusRateBtn) {
            setIconResource(iconAndColors.first)
            backgroundTintList = ContextCompat.getColorStateList(binding.root.context, iconAndColors.second)
            setIconTintResource(iconAndColors.third)
            val colorDividerAttr = android.util.TypedValue()
            binding.root.context.theme.resolveAttribute(R.attr.colorDivider, colorDividerAttr, true)
            setRippleColorResource(colorDividerAttr.resourceId)
        }
    }
}