package com.gnoemes.shikimori.presentation.view.rates.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.common.presentation.SortAction
import com.gnoemes.shikimori.entity.rates.domain.RateListAction
import com.gnoemes.shikimori.entity.rates.presentation.RateSortViewModel
import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.SeriesPlaceholderAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.ItemTouchHelperAdapter
import java.util.*

class RateAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        private val listActionCallback: (RateListAction) -> Unit,
        callback: (DetailsAction) -> Unit,
        sortCallback: (SortAction) -> Unit
) : BasePaginationAdapter(), ItemTouchHelperAdapter {

    init {
        items = mutableListOf()
        delegatesManager.apply {
            addDelegate(RateAdapterDelegate(imageLoader, navigationCallback, callback, listActionCallback))
            addDelegate(ProgressAdapterDelegate())
            addDelegate(RateSortAdapterDelegate(sortCallback))
            addDelegate(SeriesPlaceholderAdapterDelegate())
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        newItem is RateViewModel && oldItem is RateViewModel -> newItem.id == oldItem.id
        newItem is RateSortViewModel && oldItem is RateSortViewModel -> newItem.currentSort == oldItem.currentSort
        else -> newItem is ProgressItem && oldItem is ProgressItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        newItem is RateViewModel && oldItem is RateViewModel -> newItem == oldItem
        newItem is RateSortViewModel && oldItem is RateSortViewModel -> newItem == oldItem
        else -> false
    }

    override fun canDragItem(position: Int): Boolean {
        val item = items.getOrNull(position)
        return !(item == null || item !is RateViewModel || !item.isPinned)
    }

    override fun onItemMove(oldPosition: Int, newPosition: Int): Boolean {
        if (newPosition == 0) return false

        val item = items.getOrNull(newPosition)
        if (item == null || item !is RateViewModel || !item.isPinned) return false

        if (oldPosition < newPosition) {
            for (i in oldPosition until newPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in oldPosition downTo newPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(oldPosition, newPosition)
        listActionCallback.invoke(RateListAction.ChangeOrder(item, newPosition))
        return true
    }

    override fun onSelectedItemMove(newPosition: Int) {
        val item = items.getOrNull(newPosition)
        if (item == null || item !is RateViewModel || !item.isPinned) return

        listActionCallback.invoke(RateListAction.ChangeOrder(item, newPosition - 1))
    }

    override fun onItemDismiss(pos: Int, direction: Int) {
    }
}