package com.gnoemes.shikimori.presentation.view.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class SearchAdapter(
        imageLoader: ImageLoader,
        callback: (Type, Long) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        delegatesManager.apply {
            addDelegate(SearchItemAdapterDelegate(imageLoader, callback))
            addDelegate(ProgressAdapterDelegate())
        }
        items = mutableListOf()
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    fun bindItems(newItems: List<SearchItem>) {
        val oldData = items.toList()
        val progress = isProgress()

        items.clear()
        items.addAll(newItems)
        if (progress) items.add(ProgressItem())

        DiffUtil
                .calculateDiff(DiffCallback(items, oldData), false)
                .dispatchUpdatesTo(this)
    }

    fun showProgress(isVisible: Boolean) {
        val oldData = items.toList()
        val currentProgress = isProgress()

        if (isVisible && !currentProgress) {
            items.add(ProgressItem())
            notifyItemInserted(items.lastIndex)
        } else if (!isVisible && currentProgress) {
            items.remove(items.last())
            notifyItemRemoved(oldData.lastIndex)
        }
    }

    fun isProgress() = items.isNotEmpty() && items.last() is ProgressItem

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
                oldItem is SearchItem && newItem is SearchItem -> oldItem.id == newItem.id && oldItem.type == newItem.type
                else -> oldItem is ProgressItem && newItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return when {
                oldItem is SearchItem && newItem is SearchItem -> oldItem == newItem
                else -> false
            }
        }
    }

}