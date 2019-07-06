package com.gnoemes.shikimori.data.repository.progress

import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import io.reactivex.Completable
import io.reactivex.Single

interface AnimeProgressRepository {

    fun getTranslationSettings(animeId: Long): Single<TranslationSetting>

    fun saveTranslationSettings(settings : TranslationSetting): Completable
}