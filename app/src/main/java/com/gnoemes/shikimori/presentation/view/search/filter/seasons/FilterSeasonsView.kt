package com.gnoemes.shikimori.presentation.view.search.filter.seasons

import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFilterView

interface FilterSeasonsView : BaseFilterView {

    fun showSimpleData(items : List<FilterViewModel>)

    fun showCustomData(items: List<Any>)
}