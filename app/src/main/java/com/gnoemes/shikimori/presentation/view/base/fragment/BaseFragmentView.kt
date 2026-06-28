package com.gnoemes.shikimori.presentation.view.base.fragment

import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseFragmentView : BaseNetworkView {
    fun onBackPressed()
}