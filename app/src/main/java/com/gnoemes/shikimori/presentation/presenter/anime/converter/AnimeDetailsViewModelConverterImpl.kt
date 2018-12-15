package com.gnoemes.shikimori.presentation.presenter.anime.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsOptionsItem
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.nullIfEmpty
import com.gnoemes.shikimori.utils.unknownIfZero
import javax.inject.Inject

class AnimeDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource,
        private val converter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : AnimeDetailsViewModelConverter {

    override fun convertHead(it: AnimeDetails): DetailsHeadItem {

        val name = if (settings.isRomadziNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        val nameSecond = if (settings.isRomadziNaming) it.nameRu ?: it.name else it.name

        val type = convertType(it.type, it.episodes, it.duration)
        val season = converter.convertAnimeSeasonToString(it.dateAired)
        val status = convertStatus(it.status)

        val rating = convertRating(it.ageRating)

        return DetailsHeadItem(
                Type.ANIME,
                name,
                nameSecond,
                it.image,
                type,
                season,
                status,
                rating,
                it.score,
                it.genres,
                it.studios.firstOrNull()
        )
    }

    override fun convertDescriptionItem(description: String?): DetailsDescriptionItem {
        val processedText = description?.let { textProcessor.process(it) }
        return DetailsDescriptionItem(processedText)
    }

    override fun convertOptions(it: AnimeDetails, isGuest: Boolean): DetailsOptionsItem {
        return DetailsOptionsItem(
                it.userRate?.status,
                true,
                isGuest,
                context.getString(R.string.details_watch_online),
                context.getString(R.string.common_chronology
                )
        )
    }

    private fun convertStatus(status: Status): String {
        return when (status) {
            Status.ANONS -> context.getString(R.string.status_anons)
            Status.ONGOING -> context.getString(R.string.status_ongoing)
            Status.RELEASED -> context.getString(R.string.status_released)
            else -> context.getString(R.string.error_no_data)
        }
    }

    private fun convertType(type: AnimeType, episodes: Int, duration: Int): String {
        return String.format(context.getString(R.string.type_pattern), type.type.toUpperCase(),
                episodes.unknownIfZero(), duration)
    }

    private fun convertRating(ageRating: AgeRating): String? {
        val names = context.resources.getStringArray(R.array.age_ratings_values)
        val values = AgeRating.values().filter { it.rating != AgeRating.NONE.rating }
        return names.zip(values)
                .find { it.second == ageRating }
                ?.first
    }
}
