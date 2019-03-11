package com.gnoemes.shikimori.presentation.view.search

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationView

interface SearchView : BasePaginationView {

    fun selectType(newTypePos: Int)

    fun setSimpleEmptyText()

    fun setDefaultEmptyText()

    @StateStrategyType(SkipStrategy::class)
    fun showFilter(type: Type, filters: HashMap<String, MutableList<FilterItem>>)

    fun showFilterButton()

    fun hideFilterButton()

    fun addBackButton()

    fun updateFilterIcon(empty: Boolean)
}