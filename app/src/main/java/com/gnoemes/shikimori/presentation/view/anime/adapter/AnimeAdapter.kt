package com.gnoemes.shikimori.presentation.view.anime.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.presentation.AnimeHeadItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsMoreItem
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsContentAdapterDelegate
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsDescriptionAdapterDelegate
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsMoreAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class AnimeAdapter(
        imageLoader: ImageLoader,
        detailsCallback: (DetailsAction) -> Unit,
        navigationCallback: (Type, Long) -> Unit,
        settings: SettingsSource
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        with(delegatesManager) {
            addDelegate(AnimeHeadAdapterDelegate(imageLoader, detailsCallback, settings))
            addDelegate(DetailsMoreAdapterDelegate(detailsCallback))
            addDelegate(DetailsDescriptionAdapterDelegate())
            addDelegate(DetailsContentAdapterDelegate(imageLoader, settings, navigationCallback, detailsCallback))
        }

        items = mutableListOf()
    }

    fun bindItems(newItems: List<Any>) {
        val oldData = items.toMutableList()
        items.clear()
        items.addAll(newItems)

        DiffUtil
                .calculateDiff(DiffCallback(items, oldData), false)
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
            private val oldItems: MutableList<Any>,
            private val newItems: MutableList<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is AnimeHeadItem && oldItem is AnimeHeadItem) {
                newItem.name == oldItem.name
            } else if (newItem is DetailsDescriptionItem && oldItem is DetailsDescriptionItem) {
                newItem.description == oldItem.description
            } else if (newItem is DetailsMoreItem && oldItem is DetailsMoreItem) {
                newItem.type == oldItem.type
            } else if (newItem is DetailsContentItem.Loading && oldItem is DetailsContentItem.Loading) {
                newItem.contentType == oldItem.contentType
            } else if (newItem is DetailsContentItem.Content && oldItem is DetailsContentItem.Content) {
                newItem.contentType == oldItem.contentType
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is AnimeHeadItem && oldItem is AnimeHeadItem) {
                newItem == oldItem
            } else if (newItem is DetailsDescriptionItem && oldItem is DetailsDescriptionItem) {
                newItem == oldItem
            } else if (newItem is DetailsMoreItem && oldItem is DetailsMoreItem) {
                newItem == oldItem
            } else if (newItem is DetailsContentItem.Loading && oldItem is DetailsContentItem.Loading) {
                newItem == oldItem
            } else if (newItem is DetailsContentItem.Content && oldItem is DetailsContentItem.Content) {
                newItem == oldItem
            } else {
                false
            }
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            if (newItem is AnimeHeadItem && oldItem is AnimeHeadItem && newItem.rateStatus != oldItem.rateStatus) {
                return oldItem.rateStatus
            }

            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}