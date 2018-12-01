package com.gnoemes.shikimori.presentation.view.rates

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface RatesContainerView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showStatusFragment(id: Long, type: Type, status: RateStatus)

    fun setNavigationItems(items: List<Pair<RateStatus, String>>)
}