package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataJsonModel
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface OkVideoConverter {

    fun convertTracks(it : TranslationVideo, playerData: OkPlayerDataJsonModel) : Video

    fun parsePlaylists(html: String): OkPlayerDataJsonModel
}