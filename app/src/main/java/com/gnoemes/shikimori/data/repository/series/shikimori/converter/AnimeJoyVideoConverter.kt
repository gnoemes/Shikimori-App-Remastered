package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.AnimeJoyFileResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface AnimeJoyVideoConverter {

    fun convertTracks(it : TranslationVideo, videos: AnimeJoyFileResponse) : Video

    fun parsePlaylists(embedUrl: String?): AnimeJoyFileResponse
}