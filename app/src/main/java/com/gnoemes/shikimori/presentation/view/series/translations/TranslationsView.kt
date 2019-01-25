package com.gnoemes.shikimori.presentation.view.series.translations

import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.presenter.common.AddToEndSingleTagStrategy
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesView

interface TranslationsView : BaseSeriesView {

    fun showData(newItems: List<TranslationViewModel>)

    fun setEpisodeName(index: Int)

    fun setTranslationType(type: TranslationType)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search")
    fun showSearchView()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search")
    fun onSearchClosed()

}