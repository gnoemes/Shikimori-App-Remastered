package com.gnoemes.shikimori.presentation.view.details

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface BaseDetailsView : BaseFragmentView {

    fun setHeadItem(item : DetailsHeadItem)

    fun setInfoItem(item: DetailsInfoItem)

    fun setDescriptionItem(item : DetailsDescriptionItem)

    fun setContentItem(type: DetailsContentType, item : DetailsContentItem)

    fun setActionItem(item : DetailsActionItem)

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(title : String, userRate: UserRate?)

    @StateStrategyType(SkipStrategy::class)
    fun showStatusDialog(id : Long, name : String, currentStatus: RateStatus?, isAnime : Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showLinks(it: List<Link>)

    @StateStrategyType(SkipStrategy::class)
    fun showChronology(it: List<Pair<String, String>>)

    @StateStrategyType(SkipStrategy::class)
    fun showStatistic(title : String, scores : List<UserStatisticItem>, rates : List<UserStatisticItem>)
}