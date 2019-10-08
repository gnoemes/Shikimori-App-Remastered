package com.gnoemes.shikimori.presentation.presenter.chronology.converter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.AnimeWithStatus
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.chronology.ChronologyItem
import com.gnoemes.shikimori.entity.chronology.ChronologyViewModel
import com.gnoemes.shikimori.entity.common.domain.RelationType
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.manga.domain.MangaWithStatus
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.getCurrentTheme
import javax.inject.Inject

class ChronologyViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : ChronologyViewModelConverter {

    private val isDarkTheme by lazy { context.getCurrentTheme != R.style.ShikimoriAppTheme_Default }

    override fun apply(t: List<ChronologyItem>, isGuest: Boolean): List<ChronologyViewModel> {
        return t.mapNotNull { convertNode(it, isGuest) }
    }

    private fun convertNode(it: ChronologyItem, isGuest: Boolean): ChronologyViewModel? = when {
        it.item is Anime -> convertAnime(AnimeWithStatus(it.item, it.rateId, it.rateStatus), it.relation, isGuest)
        it.item is Manga -> convertManga(MangaWithStatus(it.item, it.rateId, it.rateStatus), it.relation, isGuest)
        else -> null
    }

    private fun convertAnime(it: AnimeWithStatus, relationType: RelationType, isGuest: Boolean): ChronologyViewModel {
        val name = if (settings.isRussianNaming) it.anime.nameRu ?: it.anime.name else it.anime.name
        val relation = getRelation(relationType)

        val builder = SpannableStringBuilder()

        fun appendDotIfNotEmpty() {
            if (builder.isNotEmpty()) {
                val color = if (isDarkTheme) Color.WHITE else Color.BLACK
                val divider = "  •  ".colorSpan(ColorUtils.setAlphaComponent(color, 97))
                builder.append(divider)
            }
        }

        when (it.anime.status) {
            Status.ANONS -> builder.append(context.getString(R.string.status_anons).colorSpan(context.color(R.color.status_anons)))
            Status.ONGOING -> builder.append(context.getString(R.string.status_ongoing).colorSpan(context.color(R.color.status_ongoing)))
            else -> Unit
        }

        if (it.anime.status != Status.ONGOING) {
            if (it.anime.dateReleased != null) {
                appendDotIfNotEmpty()
                builder.append(it.anime.dateReleased.year.toString())
            } else if (it.anime.dateAired != null) {
                appendDotIfNotEmpty()
                builder.append(it.anime.dateAired.year.toString())
            }
        }

        val type = getLocalizedType(it.anime.type)

        if (type != null) {
            appendDotIfNotEmpty()
            builder.append(type)
        }

        if (it.anime.type == AnimeType.MOVIE) {
            //Not implemented
        } else if (it.anime.status == Status.ANONS) {
            if (it.anime.episodes != 0) {
                appendDotIfNotEmpty()
                builder.append("${it.anime.episodes}")
            }
        } else if (it.anime.status == Status.ONGOING) {
            appendDotIfNotEmpty()
            builder.append("${it.anime.episodesAired}/${it.anime.episodes.invalidIfNullOrZero()} ${context.getString(R.string.episode_short)}")
        } else {
            appendDotIfNotEmpty()
            builder.append("${it.anime.episodes} ${context.getString(R.string.episode_short)}")
        }

        return ChronologyViewModel(
                it.anime.id,
                it.rateId ?: Constants.NO_ID,
                it.anime.linkedType,
                name,
                builder,
                it.anime.image,
                relation,
                it.status,
                isGuest
        )
    }

    private fun getRelation(relation: RelationType): String =
            when (relation) {
                RelationType.PREQUEL -> context.getString(R.string.chronology_prequel)
                RelationType.SEQUEL -> context.getString(R.string.chronology_sequel)
                RelationType.SUMMARY -> context.getString(R.string.chronology_summary)
                RelationType.FULL_STORY -> context.getString(R.string.chronology_parent_story)
                RelationType.PARENT_STORY -> context.getString(R.string.chronology_parent_story)
                RelationType.ALTERNATIVE_VERSION -> context.getString(R.string.chronology_alternative_version)
                RelationType.ALTERNATIVE_SETTING -> context.getString(R.string.chronology_alternative_setting)
                RelationType.SIDE_STORY -> context.getString(R.string.chronology_side_story)
                RelationType.OTHER -> context.getString(R.string.chronology_other)
                RelationType.NONE -> ""
            }

    private fun convertManga(it: MangaWithStatus, relationType: RelationType, isGuest: Boolean): ChronologyViewModel {
        val name = if (settings.isRussianNaming) it.manga.nameRu ?: it.manga.name else it.manga.name
        val relation = getRelation(relationType)

        val builder = SpannableStringBuilder()

        fun appendDotIfNotEmpty() {
            if (builder.isNotEmpty()) {
                val color = if (isDarkTheme) Color.WHITE else Color.BLACK
                val divider = "  •  ".colorSpan(ColorUtils.setAlphaComponent(color, 97))
                builder.append(divider)
            }
        }

        when (it.manga.status) {
            Status.ANONS -> builder.append(context.getString(R.string.status_anons).colorSpan(context.color(R.color.status_anons)))
            Status.ONGOING -> builder.append(context.getString(R.string.status_ongoing).colorSpan(context.color(R.color.status_ongoing)))
            else -> Unit
        }

        if (it.manga.status != Status.ONGOING) {
            if (it.manga.dateReleased != null) {
                builder.append(it.manga.dateReleased.year.toString())
            } else if (it.manga.dateAired != null) {
                builder.append(it.manga.dateAired.year.toString())
            }
        }

        val type = getLocalizedType(it.manga.type)

        if (type != null) {
            appendDotIfNotEmpty()
            builder.append(type)
        }

        if (it.manga.status == Status.ANONS) {
            if (it.manga.chapters != 0) {
                appendDotIfNotEmpty()
                builder.append("${it.manga.chapters}")
            }
        } else if (it.manga.status == Status.ONGOING) {
            if (it.manga.chapters != 0) {
                appendDotIfNotEmpty()
                builder.append(context.resources.getQuantityString(R.plurals.chapters, it.manga.chapters, it.manga.chapters))
            }
        } else {
            appendDotIfNotEmpty()
            builder.append(context.resources.getQuantityString(R.plurals.chapters, it.manga.chapters, it.manga.chapters))
        }


        return ChronologyViewModel(
                it.manga.id,
                it.rateId ?: Constants.NO_ID,
                it.manga.linkedType,
                name,
                builder,
                it.manga.image,
                relation,
                it.status,
                isGuest
        )
    }

    private fun getLocalizedType(type: AnimeType): String? {
        return when (type) {
            AnimeType.TV -> context.getString(R.string.type_tv_short_translatable)
            AnimeType.OVA -> context.getString(R.string.type_ova)
            AnimeType.ONA -> context.getString(R.string.type_ona)
            AnimeType.MUSIC -> context.getString(R.string.type_music_translatable)
            AnimeType.MOVIE -> context.getString(R.string.type_movie_translatable)
            AnimeType.SPECIAL -> context.getString(R.string.type_special_translatable)
            else -> null
        }
    }

    private fun getLocalizedType(type: MangaType): String? {
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

    private fun Int?.invalidIfNullOrZero(): String = if (this == null || this == 0) "-" else this.toString()

}