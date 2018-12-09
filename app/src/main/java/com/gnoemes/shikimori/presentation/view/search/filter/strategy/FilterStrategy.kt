package com.gnoemes.shikimori.presentation.view.search.filter.strategy

import com.gnoemes.shikimori.entity.common.domain.FilterItem

interface FilterStrategy {

    fun init(appliedFilters: HashMap<String, MutableList<FilterItem>>)

    fun reset()
}