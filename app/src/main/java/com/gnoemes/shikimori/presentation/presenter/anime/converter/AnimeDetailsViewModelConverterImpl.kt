package com.gnoemes.shikimori.presentation.presenter.anime.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.presentation.AnimeHeadItem
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsMoreItem
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.unknownIfZero
import javax.inject.Inject

class AnimeDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val converter: DateTimeConverter
) : AnimeDetailsViewModelConverter {

    override fun apply(t: AnimeDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        items.add(DetailsMoreItem())

        if (!t.description.isNullOrBlank()) {
            items.add(DetailsDescriptionItem(t.description!!))
        }

        if (t.videos != null && t.videos.isNotEmpty()) {
            items.add(DetailsContentItem.Content(DetailsContentType.VIDEO, t.videos))
        }

        items.add(DetailsContentItem.Loading(DetailsContentType.CHARACTERS))
        items.add(DetailsContentItem.Loading(DetailsContentType.SIMILAR))
        items.add(DetailsContentItem.Loading(DetailsContentType.RELATED))

        return items
    }

    override fun convertCharacters(it: List<Character>): Any {
        return convertItemWithType(it, DetailsContentType.CHARACTERS)
    }

    override fun convertSimilar(it: List<Anime>): Any {
        return convertItemWithType(it, DetailsContentType.SIMILAR)
    }

    override fun convertRelated(it: List<Related>): Any {
        return convertItemWithType(it, DetailsContentType.RELATED)
    }

    private fun convertItemWithType(it: List<Any>, type: DetailsContentType): Any {
        return if (it.isNotEmpty()) {
            DetailsContentItem.Content(type, it)
        } else {
            DetailsContentItem.Empty(type)
        }
    }

    private fun convertHeadItem(t: AnimeDetails): AnimeHeadItem =
            AnimeHeadItem(
                    t.name,
                    t.nameRu,
                    t.image,
                    convertType(t.type, t.episodes, t.duration),
                    converter.convertAnimeSeasonToString(t.dateAired),
                    convertStatus(t.status),
                    t.score,
                    t.userRate?.status,
                    t.genres,
                    t.studios.firstOrNull()
            )

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
}
