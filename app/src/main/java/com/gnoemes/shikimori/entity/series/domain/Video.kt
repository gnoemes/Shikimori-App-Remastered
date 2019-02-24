package com.gnoemes.shikimori.entity.series.domain

data class Video(
        val animeId: Long,
        val episodeId: Long,
        val player : String,
        val hosting: VideoHosting,
        val tracks: List<Track>
)