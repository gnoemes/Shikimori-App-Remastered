package com.gnoemes.shikimori.presentation.view.search.filter

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFilterView

interface FilterView : BaseFilterView {

    fun setSortFilters(items: List<FilterItem>, selected : Int)

    @StateStrategyType(SkipStrategy::class)
    fun showGenresDialog(type: Type, filters: HashMap<String, MutableList<FilterItem>>)

    @StateStrategyType(SkipStrategy::class)
    fun showSeasonsDialog(type: Type, filters: HashMap<String, MutableList<FilterItem>>)
}