package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import io.reactivex.Completable
import io.reactivex.Observable

interface SeriesSyncInteractor {
    fun getChanges(): Observable<List<EpisodeChanges.Changes>>
    fun sendChanges(changes : EpisodeChanges): Completable

    fun syncEpisodes(changes : List<EpisodeChanges.Changes>): Completable
}