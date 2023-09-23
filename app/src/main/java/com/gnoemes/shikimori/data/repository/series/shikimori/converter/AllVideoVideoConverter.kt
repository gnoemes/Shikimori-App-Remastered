package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.AllVideoFileResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface AllVideoVideoConverter {

    fun convertTracks(it : TranslationVideo, videos: AllVideoFileResponse) : Video

    fun parsePlaylists(html: String?): AllVideoFileResponse
}