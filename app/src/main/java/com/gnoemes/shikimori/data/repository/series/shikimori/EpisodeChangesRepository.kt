package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import io.reactivex.Completable
import io.reactivex.Observable

interface EpisodeChangesRepository {

    fun getEpisodesChanges(): Observable<EpisodeChanges>

    fun sendEpisodeChanges(changes: EpisodeChanges): Completable
}