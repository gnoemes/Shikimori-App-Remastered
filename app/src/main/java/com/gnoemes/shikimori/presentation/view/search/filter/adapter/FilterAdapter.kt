package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterNestedViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterWithButtonsViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter

class FilterAdapter(
        private val actionCallback: (FilterType, FilterAction) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit,
        private val invertCallback: (FilterType, FilterViewModel) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(FilterNestedAdapterDelegate(actionCallback))
            addDelegate(FilterWithButtonsAdapterDelegate(invertCallback, selectCallback, actionCallback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterWithButtonsViewModel && newItem is FilterWithButtonsViewModel -> oldItem.type == newItem.type
        oldItem is FilterNestedViewModel && newItem is FilterNestedViewModel -> oldItem.type == newItem.type
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterWithButtonsViewModel && newItem is FilterWithButtonsViewModel -> oldItem == newItem
        oldItem is FilterNestedViewModel && newItem is FilterNestedViewModel -> oldItem == newItem
        else -> false
    }
}