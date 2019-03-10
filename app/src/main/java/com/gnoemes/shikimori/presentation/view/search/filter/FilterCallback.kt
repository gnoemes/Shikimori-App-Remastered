package com.gnoemes.shikimori.presentation.view.search.filter

import com.gnoemes.shikimori.entity.common.domain.FilterItem

interface FilterCallback {

    fun onFiltersSelected(tag : String?, appliedFilters: HashMap<String, MutableList<FilterItem>>)
}