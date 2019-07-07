package com.gnoemes.shikimori.presentation.presenter.series.episodes.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import javax.inject.Inject

class EpisodeViewModelConverterImpl @Inject constructor() : EpisodeViewModelConverter {

    override fun convert(t: List<Episode>, currentEpisode: Int, userStatus: UserStatus): List<EpisodeViewModel> =
            t.asSequence()
                    .map { convertEpisode(it, currentEpisode, userStatus) }
                    .sortedBy { it.index }
                    .toMutableList()


    private fun convertEpisode(it: Episode, currentEpisode: Int, userStatus: UserStatus): EpisodeViewModel {
        return EpisodeViewModel(
                it.id,
                it.index,
                it.animeId,
                it.types,
                convertState(it.isWatched),
                it.isWatched,
                it.isFromAlternative,
                it.index == currentEpisode,
                userStatus == UserStatus.GUEST
        )
    }

    private fun convertState(watched: Boolean): EpisodeViewModel.State {
        return if (watched) EpisodeViewModel.State.Checked
        else EpisodeViewModel.State.NotChecked
    }
}