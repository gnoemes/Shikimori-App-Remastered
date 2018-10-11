package com.gnoemes.shikimori.entity.calendar.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class CalendarResponse(
        @field:SerializedName("anime") val anime : AnimeResponse,
        @field:SerializedName("next_episode") val nextEpisode : Int,
        @field:SerializedName("next_episode_at") val nextEpisodeDate: DateTime,
        @field:SerializedName("duration") val duration : String
)