package com.gnoemes.shikimori.presentation.view.rates

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface RateView : BaseFragmentView {

    fun showData(data: List<Rate>)

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(userRate: UserRate)

    fun hideList()

    fun showPageProgress(show: Boolean)

    fun showRefresh()

    fun hideRefresh()
}