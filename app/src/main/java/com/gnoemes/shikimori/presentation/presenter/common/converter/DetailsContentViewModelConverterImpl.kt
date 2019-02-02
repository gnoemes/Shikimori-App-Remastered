package com.gnoemes.shikimori.presentation.presenter.common.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideoType
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.roles.domain.Work
import com.gnoemes.shikimori.utils.unknownIfZero
import javax.inject.Inject

class DetailsContentViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : DetailsContentViewModelConverter {

    override fun apply(t: List<Any>): DetailsContentItem {
        val items = convertItems(t)
        return DetailsContentItem(items)
    }

    private fun convertItems(t: List<Any>): List<ContentItem> {
        return t.mapNotNull {
            when (it) {
                is Anime -> convertAnime(it)
                is Manga -> convertManga(it)
                is Character -> convertCharacter(it)
                is Person -> convertPerson(it)
                is Related -> convertRelated(it)
                is AnimeVideo -> convertVideo(it)
                is Work -> convertWork(it)
                else -> null
            }
        }
    }

    private fun convertWork(it: Work): ContentItem {
        val isAnime = it.type == Type.ANIME

        val type = if (isAnime) it.anime?.type?.type else it.manga?.type?.type
        val name = when (!settings.isRussianNaming) {
            true -> if (isAnime) it.anime?.name else it.manga?.name
            else -> if (isAnime) it.anime?.nameRu ?: it.anime?.name else it.manga?.nameRu
                    ?: it.manga?.name
        }

        val descriptionText = it.role

        val image = if (isAnime) it.anime?.image!! else it.manga?.image!!

        return ContentItem(
                name!!,
                type,
                image,
                descriptionText,
                it
        )
    }

    private fun convertVideo(it: AnimeVideo): ContentItem {

        val name = if (!it.name.isNullOrBlank()) it.name else context.getString(getTitleFromType(it.type))

        return ContentItem(
                name,
                it.hosting,
                Image(it.imageUrl, it.imageUrl, it.imageUrl, it.imageUrl),
                null,
                it
        )
    }

    //TODO simplify, make more readable
    private fun convertRelated(it: Related): ContentItem {
        val isAnime = it.type == Type.ANIME

        val type = if (isAnime) it.anime?.type?.type else it.manga?.type?.type
        val name = when (!settings.isRussianNaming) {
            true -> if (isAnime) it.anime?.name else it.manga?.name
            else -> if (isAnime) it.anime?.nameRu ?: it.anime?.name else it.manga?.nameRu
                    ?: it.manga?.name
        }

        val descriptionText = "${it.relationRu}\n(${
        if (isAnime) getLocalizedType(it.anime?.type)
        else getLocalizedType(it.manga?.type)}, ${(
                if (isAnime) it.anime?.dateAired
                else it.manga?.dateAired
                )?.year?.unknownIfZero()
        } г.)"

        val image = if (isAnime) it.anime?.image!! else it.manga?.image!!

        return ContentItem(
                name!!,
                type,
                image,
                descriptionText,
                it

        )
    }

    private fun convertPerson(it: Person): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                null,
                it.image,
                null,
                it
        )
    }

    private fun convertCharacter(it: Character): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                null,
                it.image,
                null,
                it
        )
    }

    private fun convertManga(it: Manga): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                it.type.type,
                it.image,
                null,
                it
        )
    }

    private fun convertAnime(it: Anime): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                it.type.type,
                it.image,
                null,
                it
        )
    }

    private fun getLocalizedType(type: AnimeType?): String? {
        return when (type) {
            AnimeType.TV -> context.getString(R.string.type_tv_translatable)
            AnimeType.OVA -> context.getString(R.string.type_ova)
            AnimeType.ONA -> context.getString(R.string.type_ona)
            AnimeType.MUSIC -> context.getString(R.string.type_music_translatable)
            AnimeType.MOVIE -> context.getString(R.string.type_movie_translatable)
            AnimeType.SPECIAL -> context.getString(R.string.type_special_translatable)
            else -> null
        }
    }

    private fun getLocalizedType(type: MangaType?): String? {
        return when (type) {
            MangaType.MANGA -> context.getString(R.string.type_manga_translatable)
            MangaType.NOVEL -> context.getString(R.string.type_novel_translatable)
            MangaType.ONE_SHOT -> context.getString(R.string.type_one_shot_translatable)
            MangaType.DOUJIN -> context.getString(R.string.type_doujin_translatable)
            MangaType.MANHUA -> context.getString(R.string.type_manhua_translatable)
            MangaType.MANHWA -> context.getString(R.string.type_manhwa_translatable)
            else -> null
        }
    }

    private fun getTitleFromType(videoType: AnimeVideoType): Int {
        return when (videoType) {
            AnimeVideoType.PROMO -> R.string.promo
            AnimeVideoType.ENDING -> R.string.ending
            AnimeVideoType.OPENING -> R.string.opening
            AnimeVideoType.OTHER -> R.string.other
        }
    }
}