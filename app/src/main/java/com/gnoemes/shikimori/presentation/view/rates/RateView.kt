package com.gnoemes.shikimori.presentation.view.rates

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationView

interface RateView : BasePaginationView {

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(userRate: UserRate)

    @StateStrategyType(SkipStrategy::class)
    fun showSortDialog(sorts: List<Triple<RateSort, String, Boolean>>)

    @StateStrategyType(SkipStrategy::class)
    fun showStatusDialog(id : Long, name : String, currentStatus: RateStatus, isAnime : Boolean)

    fun setNavigationItems(items: List<RateCategory>)

    fun selectType(type: Type)

    fun selectRateStatus(rateStatus: RateStatus)

    fun showEmptySearchView(it: List<Any>)

    @StateStrategyType(SkipStrategy::class)
    fun showPinLimitMessage()

    fun showEmptyRatesView(show : Boolean, isAnime: Boolean? = null)

    fun showNeedAuthView(show : Boolean)
}