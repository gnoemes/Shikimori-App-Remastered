package com.gnoemes.shikimori.presentation.view.details

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface BaseDetailsView : BaseFragmentView {

    fun setHeadItem(item : DetailsHeadItem)

    fun setOptionsItem(item : DetailsOptionsItem)

    fun setDescriptionItem(item : DetailsDescriptionItem)

    fun setContentItem(type: DetailsContentType, item : DetailsContentItem)

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(userRate: UserRate?)

    @StateStrategyType(SkipStrategy::class)
    fun showLinks(it: List<Pair<String, String>>)

    @StateStrategyType(SkipStrategy::class)
    fun showChronology(it: List<Pair<String, String>>)
}