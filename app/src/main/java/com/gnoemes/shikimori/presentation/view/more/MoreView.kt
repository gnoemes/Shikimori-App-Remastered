package com.gnoemes.shikimori.presentation.view.more

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface MoreView : BaseFragmentView {

    fun showData(items : List<Any>)

    @StateStrategyType(SkipStrategy::class)
    fun showAuthDialog()


}