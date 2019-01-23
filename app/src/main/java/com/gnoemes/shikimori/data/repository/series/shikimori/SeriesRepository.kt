package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Single

interface SeriesRepository {

    fun getEpisodes(id : Long, alternative : Boolean) : Single<List<Episode>>

    fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, alternative: Boolean): Single<List<Translation>>

    fun getTranslationSettings(animeId: Long, episodeIndex: Int): Single<TranslationSetting>

    fun setEpisodeStatus(animeId: Long, episodeId: Int, isWatched : Boolean): Completable

    fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean>

}