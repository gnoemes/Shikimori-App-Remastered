package com.gnoemes.shikimori.entity.series.domain

data class Translation(
        val animeId: Long,
        val episodeId: Int,
        val videoId: Long,
        val type: TranslationType,
        val quality: TranslationQuality,
        val hosting: VideoHosting,
        val author: String,
        val episodesSize: Int,
        val webPlayerUrl : String?
)