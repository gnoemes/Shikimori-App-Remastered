package com.gnoemes.shikimori.entity.series.domain

data class Video(
        val animeId: Long,
        val episodeId: Long,
        val hosting: VideoHosting,
        val tracks: List<Track>
)