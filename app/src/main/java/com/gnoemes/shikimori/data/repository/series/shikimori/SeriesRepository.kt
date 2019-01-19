package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Single

interface SeriesRepository {

    fun getEpisodes(id : Long) : Single<List<Episode>>

    fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>>

    fun setEpisodeStatus(animeId: Long, episodeId: Int, isWatched : Boolean): Completable

    fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean>

}