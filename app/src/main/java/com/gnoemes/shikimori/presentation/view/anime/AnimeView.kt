package com.gnoemes.shikimori.presentation.view.anime

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface AnimeView : BaseFragmentView {

    fun setAnime(items: List<Any>)

    fun setEpisodes(items: List<Any>)

    fun showEpisodeLoading()

    fun hideEpisodeLoading()

    @StateStrategyType(SkipStrategy::class)
    fun showRateDialog(userRate: UserRate?)

    @StateStrategyType(SkipStrategy::class)
    fun showLinks(it: List<Pair<String, String>>)

    @StateStrategyType(SkipStrategy::class)
    fun showChronology(it: List<Pair<String, String>>)
}