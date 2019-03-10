package com.gnoemes.shikimori.presentation.view.search.filter.seasons.adapter

import com.gnoemes.shikimori.entity.search.presentation.FilterEntryInput
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter

class FilterSeasonsAdapter(
        private val inputCallback: (String) -> Unit,
        private val closeCallback: (FilterEntryViewModel) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(FilterEntryAdapterDelegate(closeCallback))
            addDelegate(FilterSeasonInputAdapterDelegate(inputCallback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterEntryViewModel && newItem is FilterEntryViewModel -> oldItem.value == newItem.value
        else -> oldItem is FilterEntryInput && newItem is FilterEntryInput
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterEntryViewModel && newItem is FilterEntryViewModel -> oldItem == newItem
        else -> oldItem is FilterEntryInput && newItem is FilterEntryInput
    }
}