package com.gnoemes.shikimori.presentation.presenter.series.episodes.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.user.domain.UserStatus

interface EpisodeViewModelConverter {
    fun convert(t: List<Episode>, currentEpisode: Int, userStatus: UserStatus) :  List<EpisodeViewModel>
}