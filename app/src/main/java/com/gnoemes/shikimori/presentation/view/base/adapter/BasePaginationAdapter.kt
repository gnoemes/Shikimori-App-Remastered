package com.gnoemes.shikimori.presentation.view.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

abstract class BasePaginationAdapter : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    fun bindItems(newItems: List<Any>) {
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

    abstract fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean

    abstract fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean

    private inner class DiffCallback(
            private val newItems: List<Any>,
            private val oldItems: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return areContentsTheSame(oldItem, newItem)
        }
    }
}