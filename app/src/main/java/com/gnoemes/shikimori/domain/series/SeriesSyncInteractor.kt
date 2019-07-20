package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import io.reactivex.Completable
import io.reactivex.Observable

interface SeriesSyncInteractor {
    fun getChanges(): Observable<List<EpisodeChanges.Changes>>
    fun sendChanges(changes : EpisodeChanges): Completable

    fun syncEpisodes(changes : List<EpisodeChanges.Changes>): Completable

    fun startSync() : Completable

    fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long = Constants.NO_ID, onlyLocal: Boolean): Completable

    fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean) : Completable

    fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching : Boolean, onlyLocal : Boolean = false) : Completable
}