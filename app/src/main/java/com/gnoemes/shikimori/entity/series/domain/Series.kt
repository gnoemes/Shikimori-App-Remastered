package com.gnoemes.shikimori.entity.series.domain

data class Series(
        val episodes: List<Episode>,
        val episodesSize: Int = episodes.size,
        val errorMessage: String?,
        val hasError: Boolean = !errorMessage.isNullOrEmpty()
)