package com.gnoemes.shikimori.presentation.view.chronology

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.chronology.ChronologyType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface ChronologyView : BaseFragmentView {
    fun showData(items: List<Any>)
    @StateStrategyType(SkipStrategy::class)
    fun showStatusDialog(id: Long, title: String, status: RateStatus?, anime: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun scrollTo(pos: Int)

    @StateStrategyType(SkipStrategy::class)
    fun showTypeDialog(currentType: ChronologyType)
}