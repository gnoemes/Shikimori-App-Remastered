package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.series.domain.VideoHosting

data class TranslationVideo(
        val videoId : Long,
        val videoHosting: VideoHosting
)