package com.gnoemes.shikimori.presentation.view.main

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView

@StateStrategyType(AddToEndStrategy::class)
interface MainView : BaseView {

    @StateStrategyType(SkipStrategy::class)
    fun clearMoreBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearMainBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun searchActionOrClearSearchBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearCalendarBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun rateActionOrClearBackStack()

}