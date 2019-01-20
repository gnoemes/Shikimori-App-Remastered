package com.gnoemes.shikimori.entity.series.domain

data class Episode(
        val id: Long,
        val index: Int,
        val animeId: Long,
        val types: List<TranslationType>,
        val hostings: List<VideoHosting>,
        val rawHostings: String,
        val isWatched: Boolean,
        val isFromAlternative: Boolean = id != index.toLong()
)