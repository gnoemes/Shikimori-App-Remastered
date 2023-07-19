package com.gnoemes.shikimori.presentation.presenter.anime.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.nullIfEmpty
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Interval
import javax.inject.Inject

class AnimeDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource,
        private val dateTimeConverter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : AnimeDetailsViewModelConverter {

    override fun convertHead(it: AnimeDetails, isGuest: Boolean): DetailsHeadItem {

        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name


        return DetailsHeadItem(
                Type.ANIME,
                name,
                it.image,
                it.score,
                it.userRate?.status,
                isGuest
        )
    }

    override fun convertInfo(it: AnimeDetails, creators: List<Pair<Person, List<String>>>): DetailsInfoItem {
        val nameSecond = if (!settings.isRussianNaming) it.nameRu ?: it.name else it.name

        val tags = mutableListOf<DetailsTagItem>()

        it.studios.firstOrNull()?.let {
            tags.add(DetailsTagItem(it.id, DetailsTagItem.TagType.STUDIO, it.name, it))
        }

        it.genres.forEach {
            tags.add(DetailsTagItem(it.animeId.toLong(), DetailsTagItem.TagType.GENRE, it.russianName, it))
        }

        val info = mutableListOf<Any>()

        //ongoing or anons indicator
        if (it.status == Status.ANONS || it.status == Status.ONGOING) {
            val color = if (it.status == Status.ONGOING) R.color.status_ongoing else R.color.status_anons
            val description = convertStatus(it.status).colorSpan(context.color(color))

            info.add(InfoItem(description, context.getString(R.string.common_status)))
        }

        //release date (or next episode date for ongoings)
        if (it.status == Status.ANONS && it.dateAired != null) {
            val description = it.dateAired.toTitleDate()
            val category = context.getString(R.string.details_release_date)
            info.add(InfoItem(description, category))
        } else if (it.status == Status.ONGOING && it.nextEpisodeDate != null) {
            val now = DateTime.now()
            if (now.isBefore(it.nextEpisodeDate)) {
                val duration = Interval(now, it.nextEpisodeDate).toDuration()
                val description =
                        when {
                            duration.standardHours == 0L -> duration.toMinutes()
                            duration.standardDays == 0L -> duration.toHoursAndMinutes()
                            else -> duration.toDays()
                        }
                val category = String.format(context.getString(R.string.details_release_episode_format), it.episodesAired + 1)
                info.add(InfoItem(description, category))
            }
        } else if (it.dateAired != null && it.dateReleased != null) {

            val description = if (it.dateAired.year == it.dateReleased.year) {
                "${it.dateAired.toString("dd ${dateTimeConverter.convertShortMonth(it.dateAired)}")} - ${it.dateReleased.toTitleDate()}"
            } else {
                "${it.dateAired.toTitleDate()} - ${it.dateReleased.toTitleDate()}"
            }
            val category = context.getString(R.string.details_release_date)
            info.add(InfoItem(description, category))
        } else {
            if (it.dateAired != null) {
                val description = dateTimeConverter.convertAnimeSeasonToString(it.dateAired)
                val category = context.getString(R.string.details_release_date)
                info.add(InfoItem(description, category))
            }

            if (it.dateReleased != null) {
                val description = dateTimeConverter.convertAnimeSeasonToString(it.dateReleased)
                val category = context.getString(R.string.details_released)
                info.add(InfoItem(description, category))
            }
        }

        //type
        run {
            val description = getLocalizedType(it.type)
            val category = context.getString(R.string.common_type)
            info.add(InfoItem(description, category))
        }

        //episodes
        if (it.episodes > 1 || (it.status == Status.ONGOING && it.episodes == 0)) {
            val category = context.getString(R.string.common_episodes)
            if (it.status == Status.ONGOING) {
                val description = "${it.episodesAired}/" + if (it.episodes == 0) "-" else it.episodes.toString()
                info.add(InfoItem(description, category))
            } else {
                val description = it.episodes.toString()
                info.add(InfoItem(description, category))
            }
        }

        //duration
        if (it.duration != 0) {
            val description = Duration.standardMinutes(it.duration.toLong()).let {
                when {
                    it.standardHours == 0L -> it.toMinutes()
                    it.standardMinutes % 60 == 0L -> it.toHours()
                    else -> it.toHoursAndMinutes()
                }
            }
            val category = context.getString(R.string.details_episode_duration)
            info.add(InfoItem(description, category))
        }

        //age rating
        if (it.ageRating != AgeRating.NONE) {
            val description = convertRating(it.ageRating)
            if (description != null) {
                val category = context.getString(R.string.common_age_rating)
                info.add(InfoItem(description, category))
            }
        }

        //creators
        creators
                .asSequence()
                .filter { it.second.contains("Director") || it.second.contains("Original Creator") }
                .sortedByDescending { it.second.contains("Director") }
                .forEach {
                    val description = it.first.nameRu.nullIfEmpty() ?: it.first.name
                    val category = it.second.firstOrNull { role -> role == "Director" || role == "Original Creator" }?.let { convertRole(it) }
                    if (category != null) {
                        info.add(InfoClickableItem(it.first.id, it.first.linkedType, description, it.first.image, category))
                    }
                }

        return DetailsInfoItem(nameSecond, tags, info)
    }

    override fun getActions(status: Status): DetailsActionItem {
        val actions = DetailsActionType.values().toMutableList()

        if (status == Status.ANONS) actions.remove(DetailsActionType.SIMILAR)

        return DetailsActionItem(actions)
    }

    override fun convertScores(t: List<Statistic>): List<UserStatisticItem> {
        val sum = t.sumBy { it.value }

        return t
                .asSequence()
                .map { UserStatisticItem(it.name, it.value, it.value / sum.toFloat()) }
                .sortedByDescending { it.category.toInt() }
                .toMutableList()
    }

    override fun convertStatuses(t: List<Statistic>): List<UserStatisticItem> {
        val items = mutableListOf(
                Pair("Watching|Смотрю", UserStatisticItem(context.getString(R.string.rate_watching), 0, 0f)),
                Pair("Planned to Watch|Запланировано", UserStatisticItem(context.getString(R.string.rate_planned), 0, 0f)),
                Pair("Completed|Просмотрено", UserStatisticItem(context.getString(R.string.rate_completed), 0, 0f)),
                Pair("On Hold|Отложено", UserStatisticItem(context.getString(R.string.rate_on_hold), 0, 0f)),
                Pair("Dropped|Брошено", UserStatisticItem(context.getString(R.string.rate_dropped), 0, 0f))
        )

        val sum = t.sumBy { it.value }

        return items
                .mapNotNull { pair ->
                    val item = t.find { pair.first.split("|").contains(it.name) }
                    if (item != null) pair.second.copy(count = item.value, progress = item.value / sum.toFloat())
                    else null
                }
    }

    override fun convertDescriptionItem(description: String?): DetailsDescriptionItem {
        val processedText = description?.let { textProcessor.process(it) }
        return DetailsDescriptionItem(processedText)
    }

    private fun convertRole(it: String): String? = when (it) {
        "Director" -> context.getString(R.string.person_director)
        "Original Creator" -> context.getString(R.string.person_original_creator)
        else -> null
    }

    private fun convertStatus(status: Status): String {
        return when (status) {
            Status.ANONS -> context.getString(R.string.status_anons)
            Status.ONGOING -> context.getString(R.string.status_ongoing)
            Status.RELEASED -> context.getString(R.string.status_released)
            else -> context.getString(R.string.error_no_data)
        }
    }

    private fun getLocalizedType(type: AnimeType): String {
        return when (type) {
            AnimeType.TV -> context.getString(R.string.type_tv_short_translatable)
            AnimeType.OVA -> context.getString(R.string.type_ova)
            AnimeType.ONA -> context.getString(R.string.type_ona)
            AnimeType.MUSIC -> context.getString(R.string.type_music_translatable)
            AnimeType.MOVIE -> context.getString(R.string.type_movie_translatable)
            AnimeType.SPECIAL -> context.getString(R.string.type_special_translatable)
            else -> context.getString(R.string.type_tv_short_translatable)
        }
    }

    private fun convertRating(ageRating: AgeRating): String? {
        val names = context.resources.getStringArray(R.array.age_ratings_values)
        val values = AgeRating.values().filter { it.rating != AgeRating.NONE.rating }
        return names.zip(values)
                .find { it.second == ageRating }
                ?.first
    }

    private fun Duration.toMinutes(): String = "${standardMinutes % 60} ${context.getString(R.string.minute_short)}"
    private fun Duration.toHours(): String = "$standardHours ${context.getString(R.string.hour_short)}"
    private fun Duration.toHoursAndMinutes(): String = "$standardHours ${context.getString(R.string.hour_short)} ${toMinutes()}"
    private fun Duration.toDays(): String = context.resources.getQuantityString(R.plurals.days, standardDays.toInt(), standardDays)

    private fun DateTime.toTitleDate() = this.toString("dd ${dateTimeConverter.convertShortMonth(this)} yyyy")
}
