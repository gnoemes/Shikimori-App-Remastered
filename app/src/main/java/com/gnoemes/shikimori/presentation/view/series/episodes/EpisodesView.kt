package com.gnoemes.shikimori.presentation.view.series.episodes

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesView

interface EpisodesView : BaseSeriesView {

    fun showData(newItems: List<EpisodeViewModel>)

    @StateStrategyType(SkipStrategy::class)
    fun scrollToPosition(position: Int)
}