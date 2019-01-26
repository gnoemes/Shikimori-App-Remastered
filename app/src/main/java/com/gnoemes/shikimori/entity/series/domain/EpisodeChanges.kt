package com.gnoemes.shikimori.entity.series.domain

data class EpisodeChanges(
        val animeId: Long,
        val episodeIndex: Int,
        val isWatched: Boolean
)