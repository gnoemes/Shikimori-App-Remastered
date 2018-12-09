package com.gnoemes.shikimori.presentation.view.search.filter

import com.gnoemes.shikimori.entity.common.domain.FilterItem

interface FilterCallback {

    fun onFiltersSelected(appliedFilters: HashMap<String, MutableList<FilterItem>>)
}