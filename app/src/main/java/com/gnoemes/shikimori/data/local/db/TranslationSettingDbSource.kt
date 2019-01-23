package com.gnoemes.shikimori.data.local.db

import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import io.reactivex.Completable
import io.reactivex.Single

interface TranslationSettingDbSource {

    fun saveSetting(setting : TranslationSetting) : Completable

    fun getSetting(animeId : Long, episodeIndex : Int) : Single<TranslationSetting>
}