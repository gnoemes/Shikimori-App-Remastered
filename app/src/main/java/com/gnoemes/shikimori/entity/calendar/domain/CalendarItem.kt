package com.gnoemes.shikimori.entity.calendar.domain

import com.gnoemes.shikimori.entity.anime.domain.Anime
import org.joda.time.DateTime

data class CalendarItem(
        val anime: Anime,
        val nextEpisode: Int,
        val nextEpisodeDate: DateTime,
        val nextEpisodeEndDate: DateTime
)