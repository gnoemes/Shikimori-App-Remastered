package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.entity.series.domain.Series
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Single

interface SeriesRepository {

    fun getSeries(id: Long): Single<Series>

    fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>>

    fun setEpisodeWatched(animeId: Long, episodeId: Int): Completable

    fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean>

}