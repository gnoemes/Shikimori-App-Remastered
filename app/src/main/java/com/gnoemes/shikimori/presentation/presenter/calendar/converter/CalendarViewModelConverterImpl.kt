package com.gnoemes.shikimori.presentation.presenter.calendar.converter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.date.DateTimeUtils
import com.gnoemes.shikimori.utils.getCurrentTheme
import com.gnoemes.shikimori.utils.nullIfEmpty
import com.gnoemes.shikimori.utils.toHoursAndMinutesAndSeconds
import org.joda.time.*
import javax.inject.Inject

class CalendarViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeUtils: DateTimeUtils,
        private val dateTimeConverter: DateTimeConverter,
        private val settings: SettingsSource
) : CalendarViewModelConverter {

    private val isDarkTheme by lazy { context.getCurrentTheme != R.style.ShikimoriAppTheme_Default }
    private val dividerColor by lazy { val color = if (isDarkTheme) Color.WHITE else Color.BLACK; ColorUtils.setAlphaComponent(color, 97) }

    override fun apply(t: List<CalendarItem>): List<CalendarViewModel> {
        val list = mutableListOf<CalendarViewModel>()
        var animeList = mutableListOf<CalendarAnimeItem>()

        if (t.isNotEmpty()) {
            var groupDate = t.firstOrNull()?.nextEpisodeDate

            fun addItem(todayPastFlag: Boolean) {
                val title = if (todayPastFlag) context.getString(R.string.calendar_aired_already) else dateTimeConverter.convertCalendarDateToString(groupDate)
                list.add(CalendarViewModel(title, animeList
                        .sortedByDescending { it.status != null }
                        .sortedBy { it.status?.ordinal ?: 10 }
                ))
            }

            var todayPastFlag = convertDuration(groupDate) == null

            t.forEach { item ->
                val isTodayPastItem = convertDuration(item.nextEpisodeDate) == null
                if (!dateTimeUtils.isSameDay(groupDate, item.nextEpisodeDate) || (todayPastFlag && !isTodayPastItem && animeList.isNotEmpty())) {
                    addItem(todayPastFlag)
                    animeList = mutableListOf()
                    todayPastFlag = false
                }

                animeList.add(convertItem(item))
                groupDate = item.nextEpisodeDate
            }

            if (animeList.isNotEmpty()) {
                addItem(todayPastFlag)
            }
        }

        return list
    }

    private fun convertItem(item: CalendarItem): CalendarAnimeItem {
        val isAlreadyAired = convertDuration(item.nextEpisodeDate) == null

        val description = if (isAlreadyAired) String.format(context.getString(R.string.episode_number), item.nextEpisode).toUpperCase()
        else {
            val builder = SpannableStringBuilder("${item.nextEpisode} ${context.getString(R.string.episode_short)}".toUpperCase())

            val divider = "  •  ".colorSpan(dividerColor)
            builder.append(divider)

            val nextEpisode = if (Days.daysBetween(dateTimeUtils.nowDateTime, item.nextEpisodeDate).days > 0) {
                val days = Days.daysBetween(dateTimeUtils.nowDateTime, item.nextEpisodeDate).days
                context.resources.getQuantityString(R.plurals.days, days, days).toUpperCase()
            } else {
                val hours = Hours.hoursBetween(dateTimeUtils.nowDateTime, item.nextEpisodeDate).hours
                val mins = Minutes.minutesBetween(dateTimeUtils.nowDateTime, item.nextEpisodeDate).minutes % 60

                if (hours < 1) "${mins.addZeroIfNeed()} ${context.getString(R.string.minute_short)}".toUpperCase()
                else "${hours.addZeroIfNeed()} ${context.getString(R.string.hour_short)} ${mins.addZeroIfNeed()} ${context.getString(R.string.minute_short)}".toUpperCase()
            }

            builder.append(nextEpisode.colorSpan(dividerColor))
            builder
        }


        return CalendarAnimeItem(
                item.anime.id,
                if (!settings.isRussianNaming) item.anime.name else item.anime.nameRu.nullIfEmpty()
                        ?: item.anime.name,
                item.anime.image,
                item.anime.type != AnimeType.MOVIE && item.nextEpisode == item.anime.episodes,
                description,
                item.status
        )
    }

    private fun convertDuration(nextEpisodeDate: DateTime?): String? {
        if (nextEpisodeDate == null) return null

        val now = dateTimeUtils.nowDateTime
        return when (now.isBefore(nextEpisodeDate)) {
            true -> Interval(now, nextEpisodeDate).toDuration().toHoursAndMinutesAndSeconds()
            else -> null
        }
    }

    private fun Int.addZeroIfNeed(): String {
        return if (this < 10) "0$this"
        else this.toString()
    }
}