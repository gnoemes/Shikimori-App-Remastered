package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataVideo
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface OkVideoConverter {

    fun convertTracks(it : TranslationVideo, videos: List<OkPlayerDataVideo>) : Video

    fun parsePlaylists(html: String?): List<OkPlayerDataVideo>
}