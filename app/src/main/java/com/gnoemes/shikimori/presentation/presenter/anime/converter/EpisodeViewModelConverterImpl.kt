package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import javax.inject.Inject

class EpisodeViewModelConverterImpl @Inject constructor() : EpisodeViewModelConverter {
    override fun apply(t: List<Episode>): List<EpisodeViewModel> {
        return t.asSequence()
                .map { convertEpisode(it) }
                .sortedBy { it.index }
                .toMutableList()
    }

    private fun convertEpisode(it: Episode): EpisodeViewModel {
        return EpisodeViewModel(
                it.id,
                it.index,
                it.animeId,
                it.types,
                convertState(it.isWatched),
                it.isWatched
        )
    }

    private fun convertState(watched: Boolean): EpisodeViewModel.State {
        return if (watched) EpisodeViewModel.State.Checked
        else EpisodeViewModel.State.NotChecked
    }
}