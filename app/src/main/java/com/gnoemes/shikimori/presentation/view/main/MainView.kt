package com.gnoemes.shikimori.presentation.view.main

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView

@StateStrategyType(AddToEndStrategy::class)
interface MainView : BaseView {

    @StateStrategyType(SkipStrategy::class)
    fun clearMoreBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearMainBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearSearchBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearCalendarBackStack()

    @StateStrategyType(SkipStrategy::class)
    fun clearRatesBackStack()

}