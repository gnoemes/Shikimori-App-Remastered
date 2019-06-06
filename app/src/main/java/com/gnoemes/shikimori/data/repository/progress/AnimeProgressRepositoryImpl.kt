package com.gnoemes.shikimori.data.repository.progress

import com.gnoemes.shikimori.data.local.db.TranslationSettingDbSource
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AnimeProgressRepositoryImpl @Inject constructor(
        private val translationSettingSource: TranslationSettingDbSource
) : AnimeProgressRepository {

    override fun getTranslationSettings(animeId: Long): Single<TranslationSetting> =
            translationSettingSource.getSetting(animeId)

    override fun saveTranslationSettings(settings: TranslationSetting): Completable =
            translationSettingSource.saveSetting(settings)
}