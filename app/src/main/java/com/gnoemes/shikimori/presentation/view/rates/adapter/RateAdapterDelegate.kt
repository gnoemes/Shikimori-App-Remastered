package com.gnoemes.shikimori.presentation.view.rates.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.rates.domain.RateListAction
import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_rate.view.*

class RateAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit,
        private val callback: (DetailsAction) -> Unit,
        private val listActionCallback: (RateListAction) -> Unit
) : AbsListItemAdapterDelegate<RateViewModel, Any, RateAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is RateViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_rate))

    override fun onBindViewHolder(item: RateViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: RateViewModel

        init {
            with(itemView) {
                playBtn.onClick { callback.invoke(DetailsAction.WatchOnline(item.id)) }
                container.onClick { navigationCallback.invoke(item.type, item.contentId) }
                imageView.onClick { navigationCallback.invoke(item.type, item.contentId) }
                imageView.setOnLongClickListener { listActionCallback.invoke(RateListAction.Pin(item)); true }
                rateEditBtn.onClick { listActionCallback.invoke(RateListAction.EditRate(item)) }
            }
        }

        fun bind(item: RateViewModel) {
            this.item = item
            with(itemView) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)

                nameView.text = item.name
                ratingView.text = item.rating
                progressView.text = item.progress
                descriptionView.text = item.description

                pinView.visibleIf { item.isPinned }
                playBtn.visibleIf { item.type == Type.ANIME }
            }
        }
    }
}