package com.gnoemes.shikimori.presentation.view.base.fragment

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

//moxy can't work with generic views :(
interface BasePaginationView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<Any>)

    fun showPageLoading()

    fun hidePageLoading()

    fun onAllData()

}