package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Single

interface SeriesInteractor {

    fun getEpisodes(id : Long) : Single<List<Episode>>

    fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>>

    fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long = Constants.NO_ID): Completable

    fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long) : Completable

    fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching : Boolean) : Completable
}