package com.gnoemes.shikimori.presentation.presenter.calendar.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.date.DateTimeUtils
import com.gnoemes.shikimori.utils.toHoursAndMinutesAndSeconds
import com.gnoemes.shikimori.utils.unknownIfZero
import org.joda.time.DateTime
import org.joda.time.Interval
import javax.inject.Inject

class CalendarViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeUtils: DateTimeUtils,
        private val dateTimeConverter: DateTimeConverter,
        private val settings: SettingsSource
) : CalendarViewModelConverter {

    override fun apply(t: List<CalendarItem>): List<CalendarViewModel> {
        val list = mutableListOf<CalendarViewModel>()
        var animeList = mutableListOf<CalendarAnimeItem>()

        if (t.isNotEmpty()) {
            var groupDate = t.firstOrNull()?.nextEpisodeDate

            fun addItem() {
                list.add(CalendarViewModel(dateTimeConverter.convertCalendarDateToString(groupDate), animeList))
            }

            t.forEach { item ->
                if (!dateTimeUtils.isSameDay(groupDate, item.nextEpisodeDate)) {
                    addItem()
                    animeList = mutableListOf()
                }

                animeList.add(convertItem(item))
                groupDate = item.nextEpisodeDate
            }

            if (animeList.isNotEmpty()) {
                addItem()
            }
        }

        return list
    }

    private fun convertItem(item: CalendarItem): CalendarAnimeItem {
        val episodes = String.format(context.getString(R.string.episodes_format), item.anime.episodesAired.unknownIfZero(), item.anime.episodes.unknownIfZero())
        val episodeText = String.format(context.getString(R.string.episode_number), item.nextEpisode)
        val durationToAired = convertDuration(item.nextEpisodeDate)
        val nextEpisode =
                (if (durationToAired.isNullOrEmpty()) context.getText(R.string.calendar_must_aired)
                else String.format(context.getString(R.string.episode_in), durationToAired)).toString()

        return CalendarAnimeItem(
                item.anime.id,
                (if (!settings.isRussianNaming) item.anime.name else item.anime.nameRu
                        ?: item.anime.name).plus(episodes),
                item.anime.image,
                item.anime.type,
                item.anime.status,
                episodeText,
                nextEpisode,
                durationToAired,
                dateTimeUtils.isToday(item.nextEpisodeDate)
        )
    }

    private fun convertDuration(nextEpisodeDate: DateTime): String? {
        val now = dateTimeUtils.nowDateTime
        return when (now.isBefore(nextEpisodeDate)) {
            true -> Interval(now, nextEpisodeDate).toDuration().toHoursAndMinutesAndSeconds()
            else -> null
        }


    }
}