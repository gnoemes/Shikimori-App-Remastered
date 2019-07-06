package com.gnoemes.shikimori.presentation.view.series.episodes

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface EpisodesView : BaseFragmentView {

    fun showData(newItems: List<EpisodeViewModel>)

    fun scrollToPosition(pos : Int)

    fun showSearchEmpty()

    @StateStrategyType(SkipStrategy::class)
    fun showSearchView()

    @StateStrategyType(SkipStrategy::class)
    fun hideSearchView()

    fun showAlternativeLabel(show: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showEpisodeOptionsDialog(index: Int)

    @StateStrategyType(SkipStrategy::class)
    fun onEpisodeSelected(episodeId : Long, episode : Int, isAlternative : Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun onRateCreated(id : Long)

    @StateStrategyType(SkipStrategy::class)
    fun showSystemMessage(message: String)

}