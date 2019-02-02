package com.gnoemes.shikimori.presentation.presenter.search.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.utils.nullIfEmpty
import com.gnoemes.shikimori.utils.unknownIfZero
import javax.inject.Inject

class SearchViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : SearchViewModelConverter {
    override fun apply(t: List<Any>): List<SearchItem> =
            t.mapNotNull { convertItem(it) }

    private fun convertItem(item: Any): SearchItem? =
            when (item) {
                is Anime -> convertAnime(item)
                is Manga -> convertManga(item)
                is Character -> convertCharacter(item)
                is Person -> convertPerson(item)
                else -> null
            }

    private fun convertPerson(it: Person): SearchItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        return SearchItem(
                it.id,
                Type.PERSON,
                name,
                it.image,
                null
        )
    }

    private fun convertCharacter(it: Character): SearchItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        return SearchItem(
                it.id,
                Type.CHARACTER,
                name,
                it.image,
                null
        )
    }

    private fun convertManga(it: Manga): SearchItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        val nameText = name.plus(String.format(context.getString(R.string.volumes_format), it.volumes.unknownIfZero(), it.chapters.unknownIfZero()))
        return SearchItem(
                it.id,
                Type.MANGA,
                nameText,
                it.image,
                it.type.type
        )
    }

    private fun convertAnime(it: Anime): SearchItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        val aired = if (it.status == Status.RELEASED) it.episodes else it.episodesAired
        val nameText = name.plus(String.format(context.getString(R.string.episodes_format), aired, it.episodes.unknownIfZero()))
        return SearchItem(
                it.id,
                Type.ANIME,
                nameText,
                it.image,
                it.type.type
        )
    }
}