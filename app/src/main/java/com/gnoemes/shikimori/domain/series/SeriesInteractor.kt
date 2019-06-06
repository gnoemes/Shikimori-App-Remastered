package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface SeriesInteractor {

    fun getEpisodes(id : Long, alternative : Boolean) : Single<List<Episode>>

    fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, alternative: Boolean): Single<List<Translation>>

    fun getTranslationSettings(animeId: Long) : Single<TranslationSetting>

    fun saveTranslationSettings(settings : TranslationSetting) : Completable

    fun getVideo(payload : TranslationVideo, alternative: Boolean) : Single<Video>

    fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long = Constants.NO_ID, onlyLocal: Boolean): Completable

    fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean) : Completable

    fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching : Boolean, onlyLocal : Boolean = false) : Completable

    fun getEpisodeChanges() : Observable<EpisodeChanges>

    fun sendEpisodeChanges(changes: EpisodeChanges) : Completable

    fun getTopic(animeId: Long, episodeId: Int) : Single<Long>

    fun getFirstNotWatchedEpisodeIndex(animeId: Long) : Single<Int>
}