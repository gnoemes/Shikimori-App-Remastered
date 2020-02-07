package com.gnoemes.shikimori.data.repository.calendar.converter

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.entity.calendar.data.CalendarResponse
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import org.joda.time.DateTime
import javax.inject.Inject

class CalendarResponseConverterImpl @Inject constructor(
        private val animeConverter: AnimeResponseConverter
) : CalendarResponseConverter {

    override fun apply(t: List<CalendarResponse>): List<CalendarItem> =
            t.map { convertResponse(it) }
                    .filter { it.nextEpisodeDate != null }

    private fun convertResponse(it: CalendarResponse): CalendarItem = CalendarItem(
            animeConverter.convertResponse(it.anime)!!,
            it.nextEpisode,
            it.nextEpisodeDate,
            convertDuration(it.nextEpisodeDate, it.duration)
    )

    private fun convertDuration(date: DateTime?, duration: String?): DateTime? {
        if (date == null || duration == null) return null

        return when (duration.contains("/")) {
            true -> DateTime(date).plusMinutes(duration.substring(0, duration.indexOf("/")).toDouble().toInt())
            else -> DateTime(date).plusSeconds(duration.toDouble().toInt())
        }
    }
}