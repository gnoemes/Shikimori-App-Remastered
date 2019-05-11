package com.gnoemes.shikimori.presentation.view.rates.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.common.presentation.SortItem
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.gnoemes.shikimori.presentation.view.common.adapter.SortAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class RateAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        callback: (DetailsAction) -> Unit,
        sortCallback: (RateSort, Boolean) -> Unit,
        private val nextPageListener: () -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        delegatesManager.apply {
            addDelegate(RateAdapterDelegate(imageLoader, navigationCallback, callback))
            addDelegate(ProgressAdapterDelegate())
            addDelegate(SortAdapterDelegate(sortCallback))
        }
    }

    fun setData(data: List<Any>) {
        val oldData = items.toList()
        val progress = isProgress()

        items.clear()
        items.addAll(data)
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

    private fun isProgress() = items.isNotEmpty() && items.last() is ProgressItem

    override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
            payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (position == items.size - 5) nextPageListener()
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

            return if (newItem is Rate && oldItem is Rate) {
                newItem.id == oldItem.id
            } else if (newItem is SortItem && oldItem is SortItem) {
                newItem.currentSort == oldItem.currentSort
            } else {
                newItem is ProgressItem && oldItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is Rate && oldItem is Rate) {
                newItem == oldItem
            } else if (newItem is SortItem && oldItem is SortItem) {
                newItem == oldItem
            } else {
                false
            }
        }
    }
}