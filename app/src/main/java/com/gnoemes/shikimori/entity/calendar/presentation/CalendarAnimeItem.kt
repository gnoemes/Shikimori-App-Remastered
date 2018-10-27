package com.gnoemes.shikimori.entity.calendar.presentation

import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Status

data class CalendarAnimeItem(
        val id: Long,
        val name: String,
        val image: Image,
        val type: AnimeType,
        val status: Status,
        val episodeText: String,
        val nextEpisode: String,
        val durationToAired: String?,
        val isToday: Boolean
)