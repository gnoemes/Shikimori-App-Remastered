package com.gnoemes.shikimori.presentation.view.more

import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface MoreView : BaseFragmentView {

    fun showData(items : List<Any>)

    @StateStrategyType(SkipStrategy::class)
    fun showAuthDialog()


}