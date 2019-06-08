package com.gnoemes.shikimori.presentation.view.rates.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.common.presentation.SortAction
import com.gnoemes.shikimori.entity.rates.presentation.RateSortViewModel
import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.ProgressAdapterDelegate
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.SeriesPlaceholderAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader

class RateAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        callback: (DetailsAction) -> Unit,
        sortCallback: (SortAction) -> Unit
) : BasePaginationAdapter() {

    init {
        items = mutableListOf()
        delegatesManager.apply {
            addDelegate(RateAdapterDelegate(imageLoader, navigationCallback, callback))
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
}