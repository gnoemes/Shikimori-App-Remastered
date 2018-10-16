package com.gnoemes.shikimori.presentation.presenter.calendar.converter

import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.date.DateTimeUtils
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.format.PeriodFormatterBuilder
import javax.inject.Inject

class CalendarViewModelConverterImpl @Inject constructor(
        private val dateTimeUtils: DateTimeUtils,
        private val dateTimeConverter: DateTimeConverter,
        private val settings: SettingsSource
) : CalendarViewModelConverter {

    private val hoursAndMinutesAndSeconds = PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSeparator(":")
            .minimumPrintedDigits(2)
            .appendMinutes()
            .appendSeparator(":")
            .minimumPrintedDigits(2)
            .appendSeconds()
            .toFormatter()


    override fun apply(t: List<CalendarItem>): List<CalendarViewModel> {
        val list = mutableListOf<CalendarViewModel>()
        var animeList = mutableListOf<CalendarAnimeItem>()

        if (t.isNotEmpty()) {
            var groupDate = t.firstOrNull()?.nextEpisodeDate

            t.forEach { item ->
                if (!dateTimeUtils.isSameDay(groupDate, item.nextEpisodeDate)) {
                    list.add(CalendarViewModel(dateTimeConverter.convertCalendarDateToString(groupDate), animeList))
                    animeList = mutableListOf()
                }

                animeList.add(convertItem(item))
                groupDate = item.nextEpisodeDate
            }
        }

        return list
    }

    private fun convertItem(item: CalendarItem): CalendarAnimeItem =
            CalendarAnimeItem(
                    item.anime.id,
                    if (settings.isRomadziNaming) item.anime.name else item.anime.nameRu
                            ?: item.anime.name,
                    item.anime.image,
                    item.anime.url,
                    item.anime.type,
                    item.anime.status,
                    item.anime.episodes,
                    item.anime.episodesAired,
                    item.nextEpisode,
                    convertDuration(item.nextEpisodeDate),
                    dateTimeUtils.isToday(item.nextEpisodeDate)
            )

    private fun convertDuration(nextEpisodeDate: DateTime): String? {
        val now = dateTimeUtils.nowDateTime
        return when (now.isBefore(nextEpisodeDate)) {
            true -> hoursAndMinutesAndSeconds.print(Interval(now, nextEpisodeDate).toDuration().toPeriod())
            else -> null
        }


    }
}