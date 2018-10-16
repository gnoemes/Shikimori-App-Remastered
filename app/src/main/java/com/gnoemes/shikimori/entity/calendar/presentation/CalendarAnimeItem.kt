package com.gnoemes.shikimori.entity.calendar.presentation

import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Status

data class CalendarAnimeItem(
        val id: Long,
        val name: String,
        val image: Image,
        val url: String,
        val type: AnimeType,
        val status: Status,
        val episodes: Int,
        val episodesAired: Int,
        val episodeNext: Int,
        val durationToAired: String?,
        val isToday: Boolean
)