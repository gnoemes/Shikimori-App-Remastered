package com.gnoemes.shikimori.presentation.view.series.translations

import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesView

interface TranslationsView : BaseSeriesView {

    fun showData(newItems : List<TranslationViewModel>)

    fun setEpisodeName(index : Int)

    fun setTranslationType(type: TranslationType)

}