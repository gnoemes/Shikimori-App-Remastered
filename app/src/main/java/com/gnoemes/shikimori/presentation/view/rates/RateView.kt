package com.gnoemes.shikimori.presentation.view.rates

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationView

interface RateView : BasePaginationView {

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(userRate: UserRate)

    @StateStrategyType(SkipStrategy::class)
    fun showSortDialog()

}