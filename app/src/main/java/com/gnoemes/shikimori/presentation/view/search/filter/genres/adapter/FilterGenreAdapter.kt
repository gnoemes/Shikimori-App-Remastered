package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterMainGenreCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterOtherGenreCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter

class FilterGenreAdapter(
        invertCallback: (FilterType, FilterViewModel) -> Unit,
        selectCallback: (FilterType, FilterViewModel) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(FilterGenreMainCategoryAdapterDelegate(invertCallback, selectCallback))
            addDelegate(FilterGenreOtherCategoryAdapterDelegate(invertCallback, selectCallback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterMainGenreCategory && newItem is FilterMainGenreCategory -> true
        else -> oldItem is FilterOtherGenreCategory && newItem is FilterOtherGenreCategory
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FilterMainGenreCategory && newItem is FilterMainGenreCategory -> oldItem == newItem
        oldItem is FilterOtherGenreCategory && newItem is FilterOtherGenreCategory -> oldItem == newItem
        else -> false
    }
}