package com.gnoemes.shikimori.presentation.view.base.fragment

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

//moxy can't work with generic views :(
interface BasePaginationView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<Any>)

    fun hideData()

    fun showPageLoading()

    fun hidePageLoading()

}