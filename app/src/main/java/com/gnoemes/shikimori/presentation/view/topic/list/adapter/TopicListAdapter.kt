package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class TopicListAdapter(
        imageLoader: ImageLoader,
        resourceProvider: TopicResourceProvider,
        converter: DateTimeConverter,
        navigationCallback: (Type, Long) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()

        delegatesManager.apply {
            addDelegate(TopicClubAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(TopicLinkedAdapterDelegate(imageLoader, converter, navigationCallback))
            addDelegate(ProgressAdapterDelegate())
            setFallbackDelegate(TopicAdapterDelegate(imageLoader, resourceProvider, navigationCallback))
        }
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
                oldItem is TopicViewModel && newItem is TopicViewModel -> oldItem.id == newItem.id
                else -> oldItem is ProgressItem && newItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return when {
                oldItem is TopicViewModel && newItem is TopicViewModel -> oldItem == newItem
                else -> false
            }
        }
    }
}