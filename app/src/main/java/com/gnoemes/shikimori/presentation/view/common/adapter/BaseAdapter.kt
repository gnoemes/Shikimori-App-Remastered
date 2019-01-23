package com.gnoemes.shikimori.presentation.view.common.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

abstract class BaseAdapter<T> :  ListDelegationAdapter<MutableList<T>>(){

    init {
        items = mutableListOf()
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    open fun bindItems(newItems: List<T>) {
        val oldData = items.toList()

        items.clear()
        items.addAll(newItems)

        DiffUtil
                .calculateDiff(DiffCallback(items, oldData), false)
                .dispatchUpdatesTo(this)
    }

    protected inner class DiffCallback(
            private val newItems: List<T>,
            private val oldItems: List<T>
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