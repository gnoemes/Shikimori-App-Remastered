package com.gnoemes.shikimori.presentation.view.rates

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface RatesContainerView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showStatusFragment(id: Long, type: Type, status: RateStatus)

    fun setNavigationItems(items: List<RateCategory>)

    fun showContainer()

    fun hideContainer()

    fun selectType(type: Type)

    @StateStrategyType(SkipStrategy::class)
    fun showRandomRate(type: Type, status: RateStatus)

    fun selectRateStatus(rateStatus: RateStatus)

}