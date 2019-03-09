package com.gnoemes.shikimori.presentation.view.search.filter

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface FilterView : BaseFragmentView {

    fun showData(items : List<Any>)

    fun setResetEnabled(show : Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>)
}