package com.gnoemes.shikimori.presentation.view.search.filter

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFilterView

interface FilterView : BaseFilterView {

    fun setSortFilters(items: List<FilterItem>, selected : Int)
}