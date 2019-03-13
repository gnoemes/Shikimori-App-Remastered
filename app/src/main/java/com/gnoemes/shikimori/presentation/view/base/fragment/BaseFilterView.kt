package com.gnoemes.shikimori.presentation.view.base.fragment

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.FilterItem

interface BaseFilterView : BaseFragmentView {

    fun showData(items: List<Any>)

    fun setResetEnabled(show: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>)
}