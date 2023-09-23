package com.gnoemes.shikimori.presentation.presenter.manga.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.nullIfEmpty
import javax.inject.Inject

class MangaDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource,
        private val dateTimeConverter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : MangaDetailsViewModelConverter {

    override fun convertHead(it: MangaDetails, isGuest: Boolean): DetailsHeadItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name

        return DetailsHeadItem(
                Type.MANGA,
                name,
                it.image,
                it.score,
                it.userRate?.status,
                isGuest
        )
    }

    override fun convertInfo(it: MangaDetails, creators: List<Pair<Person, List<String>>>): DetailsInfoItem {
        val nameSecond = if (!settings.isRussianNaming) it.nameRu.nullIfEmpty() ?: it.name else it.name

        val tags = mutableListOf<DetailsTagItem>()

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
            val description = it.dateAired.toString("dd MMMM yyyy")
            val category = context.getString(R.string.details_release_date)
            info.add(InfoItem(description, category))
        } else if (it.status == Status.ONGOING && it.dateAired != null) {
            val description = dateTimeConverter.convertAnimeSeasonToString(it.dateAired)
            val category = context.getString(R.string.details_release_date)
            info.add(InfoItem(description, category))
        } else if (it.dateReleased != null && it.dateAired != null) {
            val description = "${it.dateAired.year} - ${it.dateReleased.year}"
            val category = context.getString(R.string.details_released_manga)
            info.add(InfoItem(description, category))
        }

        //type
        run {
            val description = getLocalizedType(it.type)
            val category = context.getString(R.string.common_type)
            info.add(InfoItem(description, category))
        }

        //volumes
        if (it.volumes != 0) {
            val description = it.volumes.toString()
            val category = context.getString(R.string.common_volumes)
            info.add(InfoItem(description, category))
        }

        //chapters
        if (it.chapters != 0) {
            val description = it.chapters.toString()
            val category = context.getString(R.string.common_chapters)
            info.add(InfoItem(description, category))
        }

        creators
                .asSequence()
                .filter { it.second.contains("Art") || it.second.contains("Story") || it.second.contains("Story & Art") }
                .sortedByDescending { it.second.contains("Story") }
                .forEach {
                    val description = it.first.nameRu.nullIfEmpty() ?: it.first.name
                    val category = it.second.firstOrNull { role -> role == "Art" || role == "Story" || role == "Story & Art" }?.let { convertRole(it) }
                    if (category != null) {
                        info.add(InfoClickableItem(it.first.id, it.first.linkedType, description, it.first.image, category))
                    }
                }

        return DetailsInfoItem(nameSecond, tags, info)
    }

    override fun getActions(status : Status): DetailsActionItem {
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
                Pair("Reading|Читаю", UserStatisticItem(context.getString(R.string.rate_reading), 0, 0f)),
                Pair("Planned to Read|Запланировано", UserStatisticItem(context.getString(R.string.rate_planned), 0, 0f)),
                Pair("Completed|Прочитано", UserStatisticItem(context.getString(R.string.rate_readed), 0, 0f)),
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

    private fun convertRole(it: String): String? = when (it) {
        "Art" -> context.getString(R.string.person_art)
        "Story" -> context.getString(R.string.person_story)
        "Story & Art" -> context.getString(R.string.person_story_and_art)
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

    private fun getLocalizedType(type: MangaType): String {
        return when (type) {
            MangaType.MANGA -> context.getString(R.string.type_manga_translatable)
            MangaType.DOUJIN -> context.getString(R.string.type_doujin_translatable)
            MangaType.MANHUA -> context.getString(R.string.type_manhua_translatable)
            MangaType.MANHWA -> context.getString(R.string.type_manhwa_translatable)
            MangaType.NOVEL -> context.getString(R.string.type_novel_translatable)
            MangaType.LIGHT_NOVEL -> context.getString(R.string.type_novel_translatable)
            MangaType.ONE_SHOT -> context.getString(R.string.type_one_shot_translatable)
            MangaType.UNKNOWN -> ""
        }
    }

    override fun convertDescriptionItem(description: String?): DetailsDescriptionItem {
        val processedText = description?.let { textProcessor.process(it) }
        return DetailsDescriptionItem(processedText)
    }
}