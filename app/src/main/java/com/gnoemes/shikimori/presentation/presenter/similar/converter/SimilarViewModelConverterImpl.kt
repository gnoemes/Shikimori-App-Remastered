package com.gnoemes.shikimori.presentation.presenter.similar.converter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.AnimeWithStatus
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.manga.domain.MangaWithStatus
import com.gnoemes.shikimori.entity.similar.presentation.SimilarViewModel
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.getCurrentTheme
import javax.inject.Inject

class SimilarViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : SimilarViewModelConverter {

    private val isDarkTheme by lazy { context.getCurrentTheme != R.style.ShikimoriAppTheme_Default }

    override fun apply(t: List<Any>, isGuest: Boolean): List<SimilarViewModel> = t.mapNotNull {
        when (it) {
            is AnimeWithStatus -> convertAnime(it, isGuest)
            is MangaWithStatus -> convertManga(it, isGuest)
            else -> null
        }
    }

    private fun convertAnime(it: AnimeWithStatus, isGuest: Boolean): SimilarViewModel {
        val name = if (settings.isRussianNaming) it.anime.nameRu ?: it.anime.name else it.anime.name

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

        return SimilarViewModel(
                it.anime.id,
                it.rateId ?: Constants.NO_ID,
                it.anime.linkedType,
                name,
                builder,
                it.anime.image,
                it.anime.score,
                it.status,
                isGuest
        )
    }

    private fun convertManga(it: MangaWithStatus, isGuest: Boolean): SimilarViewModel {
        val name = if (settings.isRussianNaming) it.manga.nameRu ?: it.manga.name else it.manga.name

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


        return SimilarViewModel(
                it.manga.id,
                it.rateId ?: Constants.NO_ID,
                it.manga.linkedType,
                name,
                builder,
                it.manga.image,
                it.manga.score,
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