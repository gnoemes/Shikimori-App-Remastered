package com.gnoemes.shikimori.presentation.view.chronology.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.chronology.ChronologyViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_chronology.view.*


class ChronologyAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit,
        private val callback: (Long) -> Unit
) : AbsListItemAdapterDelegate<ChronologyViewModel, Any, ChronologyAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = item is ChronologyViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_chronology))

    override fun onBindViewHolder(item: ChronologyViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: ChronologyViewModel

        init {
            itemView.container.onClick { navigationCallback.invoke(item.type, item.id) }
            itemView.statusRateBtn.onClick { callback.invoke(item.id) }
        }

        fun bind(item: ChronologyViewModel) {
            this.item = item
            with(itemView) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)
                nameView.text = item.title
                descriptionView.text = item.description
                relationView.text = item.relation
                statusRateBtn.visibleIf { !item.isGuest }

                val iconAndColors = when (item.status) {
                    RateStatus.PLANNED -> Triple(R.drawable.ic_planned, R.color.rate_default_transparent, R.color.rate_default_dark)
                    RateStatus.WATCHING -> Triple(R.drawable.ic_play_rate, R.color.rate_default_transparent, R.color.rate_default_dark)
                    RateStatus.REWATCHING -> Triple(R.drawable.ic_replay, R.color.rate_default_transparent, R.color.rate_default_dark)
                    RateStatus.DROPPED -> Triple(R.drawable.ic_close, R.color.rate_dropped_transparent, R.color.rate_dropped_dark)
                    RateStatus.ON_HOLD -> Triple(R.drawable.ic_pause_rate, R.color.rate_on_hold_transparent, R.color.rate_on_hold_dark)
                    RateStatus.COMPLETED -> Triple(R.drawable.ic_check, R.color.rate_watched_transparent, R.color.rate_watched_dark)
                    else -> null
                }

                if (iconAndColors == null) {
                    with(statusRateBtn) {
                        setIconResource(R.drawable.ic_plus)
                        iconTint = context.colorStateList(R.color.selector_circle_button_text_color)
                        backgroundTintList = context.colorStateList(R.color.selector_gray_circle_background_color)
                        rippleColor = context.colorStateList(R.color.selector_button_ripple_color)
                    }
                } else {
                    with(statusRateBtn) {
                        setIconResource(iconAndColors.first)
                        backgroundTintList = context.colorStateList(iconAndColors.second)
                        setIconTintResource(iconAndColors.third)
                        setRippleColorResource(context.attr(R.attr.colorDivider).resourceId)
                    }
                }

            }
        }

    }
}