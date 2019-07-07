package com.gnoemes.shikimori.presentation.presenter.series.translations.converter

import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel

interface TranslationsViewModelConverter {
    fun convertTranslations(translations: List<Translation>, setting: TranslationSetting?): List<TranslationViewModel>
    fun convertTrack(video: Video, track: Track): SeriesDownloadItem
}