package com.gnoemes.shikimori.presentation.presenter.search.converter

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory

interface FilterViewModelConverter  {

    fun convert(filters : List<FilterCategory>, appliedFilters : HashMap<String, MutableList<FilterItem>>) : List<Any>

}