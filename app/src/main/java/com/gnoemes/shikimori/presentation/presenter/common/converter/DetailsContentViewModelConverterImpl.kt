package com.gnoemes.shikimori.presentation.presenter.common.converter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.*
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.FrameItem
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.roles.domain.Work
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.getCurrentTheme
import javax.inject.Inject

class DetailsContentViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : DetailsContentViewModelConverter {

    private val isDarkTheme by lazy { context.getCurrentTheme != R.style.ShikimoriAppTheme_Default }
    private val dividerColor by lazy { val color = if (isDarkTheme) Color.WHITE else Color.BLACK; ColorUtils.setAlphaComponent(color, 97) }

    override fun apply(t: List<Any>): DetailsContentItem {
        val items = convertItems(t)
        return DetailsContentItem(items)
    }

    private fun convertItems(t: List<Any>): List<Any> {
        return t.mapNotNull {
            when (it) {
                is Anime -> convertAnime(it)
                is Manga -> convertManga(it)
                is Character -> convertCharacter(it)
                is Person -> convertPerson(it)
                is Related -> convertRelated(it)
                is AnimeVideo -> convertVideo(it)
                is Work -> convertWork(it)
                is Screenshot -> convertScreenshot(it)
                is PlaceholderItem -> it
                else -> null
            }
        }
    }

    private fun convertScreenshot(it: Screenshot): FrameItem {
        val image = Image(it.original, it.preview, null, null)
        return FrameItem(image,null, null, it)
    }

    private fun convertWork(it: Work): ContentItem {
        val isAnime = it.type == Type.ANIME

        val name = when (!settings.isRussianNaming) {
            true -> if (isAnime) it.anime?.name else it.manga?.name
            else -> if (isAnime) it.anime?.nameRu ?: it.anime?.name else it.manga?.nameRu
                    ?: it.manga?.name
        }

        val descriptionText = it.role

        val image = if (isAnime) it.anime?.image!! else it.manga?.image!!

        return ContentItem(
                name!!,
                image,
                descriptionText,
                it
        )
    }

    private fun convertVideo(it: AnimeVideo): FrameItem {
        return FrameItem(
                Image(it.imageUrl, it.imageUrl, it.imageUrl, it.imageUrl),
                it.name ?: context.getString(getTitleFromType(it.type)),
                it.hosting?.toUpperCase(),
                it
        )
    }

    private fun convertRelated(it: Related): ContentItem {
        val isAnime = it.type == Type.ANIME

        val name = it.relationRu ?: it.relation
        val description = SpannableStringBuilder((if (isAnime) getLocalizedType(it.anime!!.type) else getLocalizedType(it.manga!!.type)).toUpperCase())

        val divider = "  •  ".colorSpan(dividerColor)
        if (isAnime && it.anime?.dateReleased != null) {
            description.append(divider)
            description.append("${it.anime.dateReleased.year}".colorSpan(dividerColor))
        } else if (isAnime && it.anime?.dateAired != null) {
            description.append(divider)
            description.append("${it.anime.dateAired.year}".colorSpan(dividerColor))
        } else if (!isAnime && it.manga?.dateReleased != null) {
            description.append(divider)
            description.append("${it.manga.dateReleased.year}".colorSpan(dividerColor))
        } else if (!isAnime && it.manga?.dateAired != null) {
            description.append(divider)
            description.append("${it.manga.dateAired.year}".colorSpan(dividerColor))
        }

        val image = if (isAnime) it.anime?.image!! else it.manga?.image!!

        return ContentItem(
                name,
                image,
                description,
                it

        )
    }

    private fun convertPerson(it: Person): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                it.image,
                null,
                it
        )
    }

    private fun convertCharacter(it: Character): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        return ContentItem(
                name,
                it.image,
                null,
                it
        )
    }

    private fun convertManga(it: Manga): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        val description = SpannableStringBuilder(getLocalizedType(it.type).toUpperCase())

        val divider = "  •  ".colorSpan(dividerColor)
        if (it.dateReleased != null) {
            description.append(divider)
            description.append("${it.dateReleased.year}".colorSpan(dividerColor))
        } else if (it.dateAired != null) {
            description.append(divider)
            description.append("${it.dateAired.year}".colorSpan(dividerColor))
        }

        return ContentItem(
                name,
                it.image,
                null,
                it
        )
    }

    private fun convertAnime(it: Anime): ContentItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu ?: it.name
        val description = SpannableStringBuilder(getLocalizedType(it.type).toUpperCase())

        val divider = "  •  ".colorSpan(dividerColor)
        if (it.dateReleased != null) {
            description.append(divider)
            description.append("${it.dateReleased.year}".colorSpan(dividerColor))
        } else if (it.dateAired != null) {
            description.append(divider)
            description.append("${it.dateAired.year}".colorSpan(dividerColor))
        }

        return ContentItem(
                name,
                it.image,
                description,
                it
        )
    }

    private fun getLocalizedType(type: AnimeType?): String {
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

    private fun getLocalizedType(type: MangaType?): String {
        return when (type) {
            MangaType.MANGA -> context.getString(R.string.type_manga_translatable)
            MangaType.NOVEL -> context.getString(R.string.type_novel_translatable)
            MangaType.ONE_SHOT -> context.getString(R.string.type_one_shot_translatable)
            MangaType.DOUJIN -> context.getString(R.string.type_doujin_translatable)
            MangaType.MANHUA -> context.getString(R.string.type_manhua_translatable)
            MangaType.MANHWA -> context.getString(R.string.type_manhwa_translatable)
            else -> context.getString(R.string.type_manga_translatable)
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