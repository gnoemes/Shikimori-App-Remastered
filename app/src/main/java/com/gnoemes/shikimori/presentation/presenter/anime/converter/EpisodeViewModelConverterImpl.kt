package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.series.domain.Series
import javax.inject.Inject

class EpisodeViewModelConverterImpl @Inject constructor() : EpisodeViewModelConverter {
    override fun apply(t: Series): List<Any> {
        return emptyList()
    }
}