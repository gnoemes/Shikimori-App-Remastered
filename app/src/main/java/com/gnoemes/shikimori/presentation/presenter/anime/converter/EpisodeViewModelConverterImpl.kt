package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import javax.inject.Inject

class EpisodeViewModelConverterImpl @Inject constructor() : EpisodeViewModelConverter {
    override fun apply(t: List<Episode>): List<EpisodeViewModel> {
        return t.map { convertEpisode(it) }.sortedBy { it.id }
    }

    private fun convertEpisode(it: Episode): EpisodeViewModel {
        return EpisodeViewModel(
                it.id,
                it.animeId,
                it.types,
                it.isWatched
        )
    }
}