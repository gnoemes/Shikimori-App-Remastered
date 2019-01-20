package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.series.presentation.EpisodePlaceholderItem
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class EpisodeAdapter(
        callback: (EpisodeViewModel) -> Unit,
        episodeChanged: (EpisodeViewModel, Boolean) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()

        delegatesManager.addDelegate(EpisodeAdapterDelegate(callback, episodeChanged))
        delegatesManager.addDelegate(EpisodePlaceholderAdapterDelegate())
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    fun bindItems(newItems: List<Any>) {
        val oldData = items.toList()

        items.clear()
        items.addAll(newItems)

        DiffUtil
                .calculateDiff(DiffCallback(items, oldData), false)
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
            private val newItems: List<Any>,
            private val oldItems: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return when {
                oldItem is EpisodeViewModel && newItem is EpisodeViewModel -> oldItem.index == newItem.index
                oldItem is EpisodePlaceholderItem && newItem is EpisodePlaceholderItem -> oldItem.index == newItem.index
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return when {
                oldItem is EpisodeViewModel && newItem is EpisodeViewModel -> oldItem == newItem
                oldItem is EpisodePlaceholderItem && newItem is EpisodePlaceholderItem -> oldItem == newItem
                else -> false
            }
        }
    }


}