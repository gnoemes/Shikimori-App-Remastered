package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface MyviVideoConverter {

    fun convertTracks(it : TranslationVideo, video: String?) : Video

    fun parsePlaylist(html: String?): String?
}