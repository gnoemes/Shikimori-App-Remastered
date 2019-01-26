package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting

data class TranslationVideo(
        val videoId: Long,
        val animeId : Long,
        val episodeIndex : Int,
        val language: String,
        val author: String,
        val authorSimple: String,
        val type: TranslationType,
        val videoHosting: VideoHosting
)