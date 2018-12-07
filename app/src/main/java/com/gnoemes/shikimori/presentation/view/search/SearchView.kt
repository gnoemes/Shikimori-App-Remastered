package com.gnoemes.shikimori.presentation.view.search

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface SearchView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<SearchItem>)

    fun hideData()

    fun showPageLoading()

    fun hidePageLoading()

    fun selectType(newTypePos: Int)

    fun setSimpleEmptyText()

    fun setDefaultEmptyText()
}