package com.gnoemes.shikimori.presentation.view.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate

abstract class BasePaginationAdapter : BaseAdapter<Any>() {

    init {
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    override fun getItemId(position: Int): Long {
        return items!![position].hashCode().toLong()
    }

    override fun bindItems(newItems: List<Any>) {
        val oldData = items?.toList() ?: emptyList()
        val progress = isProgress()

        items?.clear()
        items?.addAll(newItems)
        if (progress) items?.add(ProgressItem())

        DiffUtil
                .calculateDiff(DiffCallback(items ?: emptyList(), oldData), false)
                .dispatchUpdatesTo(this)
    }

    open fun showProgress(isVisible: Boolean) {
        val oldData = items?.toList() ?: emptyList()
        val currentProgress = isProgress()
        val itemsList = items

        if (isVisible && !currentProgress) {
            itemsList?.add(ProgressItem())
            notifyItemInserted(itemsList!!.lastIndex)
        } else if (!isVisible && currentProgress) {
            itemsList?.removeAt(itemsList.lastIndex)
            notifyItemRemoved(oldData.lastIndex)
        }
    }

    open fun isProgress() = items?.isNotEmpty() == true && items!!.last() is ProgressItem

}