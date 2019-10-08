package com.gnoemes.shikimori.presentation.view.similar

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface SimilarView : BaseFragmentView {

    fun showData(items: List<Any>)

    @StateStrategyType(SkipStrategy::class)
    fun showStatusDialog(id: Long, title: String, status: RateStatus?, anime: Boolean)
}