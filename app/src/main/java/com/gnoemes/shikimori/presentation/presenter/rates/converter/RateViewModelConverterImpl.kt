package com.gnoemes.shikimori.presentation.presenter.rates.converter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.colorSpan
import com.gnoemes.shikimori.utils.getCurrentTheme
import com.gnoemes.shikimori.utils.nullIfEmpty
import javax.inject.Inject

class RateViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource
) : RateViewModelConverter {

    private val isDarkTheme by lazy { context.getCurrentTheme != R.style.ShikimoriAppTheme_Default }

    override fun apply(t: List<Any>, pinned: List<PinnedRate>): List<Any> =
            t.asSequence()
                    .map {
                        if (it is Rate) convertRate(it, pinned)
                        else it
                    }.sortedBy {
                        if (it is RateViewModel) !it.isPinned
                        else false
                    }
                    .toMutableList()

    private fun convertRate(it: Rate, pinned: List<PinnedRate>): RateViewModel {
        val type = if (it.manga == null) Type.ANIME else Type.MANGA

        fun isAnime(): Boolean = type == Type.ANIME

        val contentId = if (isAnime()) it.anime!!.id else it.manga!!.id
        val image = if (isAnime()) it.anime!!.image else it.manga!!.image
        val name = if (isAnime()) {
            if (settings.isRussianNaming) it.anime!!.nameRu.nullIfEmpty() ?: it.anime.name
            else it.anime!!.name
        } else {
            if (settings.isRussianNaming) it.manga!!.nameRu.nullIfEmpty() ?: it.manga.name
            else it.manga!!.name
        }

        val description = getDescription(it)
        val progress = if (isAnime()) it.episodes.invalidIfNullOrZero() else it.chapters.invalidIfNullOrZero()

        val pinnedRate = pinned.find { contentId == it.id }
        val isPinned = pinnedRate != null

        return RateViewModel(
                it.id,
                contentId,
                type,
                image,
                name,
                description,
                it.score.invalidIfNullOrZero(),
                progress,
                it,
                isPinned,
                pinnedRate?.order ?: Constants.MAX_PINNED_RATES + 1
        )
    }

    private fun getDescription(it: Rate): CharSequence {
        val builder = SpannableStringBuilder()

        fun appendDotIfNotEmpty() {
            if (builder.isNotEmpty()) {
                val color = if (isDarkTheme) Color.WHITE else Color.BLACK
                val divider = "  •  ".colorSpan(ColorUtils.setAlphaComponent(color, 97))
                builder.append(divider)
            }
        }

        val status = if (it.anime != null) it.anime.status else it.manga!!.status

        when (status) {
            Status.ANONS -> builder.append(context.getString(R.string.status_anons).colorSpan(context.color(R.color.status_anons)))
            Status.ONGOING -> builder.append(context.getString(R.string.status_ongoing).colorSpan(context.color(R.color.status_ongoing)))
            else -> Unit
        }

        if (status != Status.ONGOING) {
            if (it.anime?.dateAired != null) {
                appendDotIfNotEmpty()
                builder.append("${it.anime.dateAired.year}")
            } else if (it.manga?.dateAired != null) {
                appendDotIfNotEmpty()
                builder.append("${it.manga.dateAired.year}")
            }
        }

        if (it.anime != null) {
            val localizedType = getLocalizedType(it.anime.type)
            if (!localizedType.isNullOrEmpty()) {
                appendDotIfNotEmpty()
                builder.append(localizedType)
            }
        } else if (it.manga != null) {
            val localizedType = getLocalizedType(it.manga.type)
            if (!localizedType.isNullOrEmpty()) {
                appendDotIfNotEmpty()
                builder.append(localizedType)
            }
        }

        if (it.anime != null && it.anime.type == AnimeType.MOVIE) {
            //Not implemented
        } else if (status == Status.ANONS) {
            if (it.anime != null && it.anime.episodes != 0) {
                appendDotIfNotEmpty()
                builder.append("${it.anime.episodes}")
            } else if (it.manga != null && it.manga.chapters != 0) {
                appendDotIfNotEmpty()
                builder.append("${it.manga.chapters}")
            }
        } else if (status == Status.ONGOING) {
            if (it.anime != null) {
                appendDotIfNotEmpty()
                builder.append("${it.anime.episodesAired}/${it.anime.episodes.invalidIfNullOrZero()} ${context.getString(R.string.episode_short)}")
            } else if (it.manga != null && it.manga.chapters != 0) {
                appendDotIfNotEmpty()
                builder.append(context.resources.getQuantityString(R.plurals.chapters, it.manga.chapters, it.manga.chapters))
            }
        } else {
            if (it.anime != null) {
                appendDotIfNotEmpty()
                builder.append("${it.anime.episodes} ${context.getString(R.string.episode_short)}")
            } else if (it.manga != null) {
                appendDotIfNotEmpty()
                builder.append(context.resources.getQuantityString(R.plurals.chapters, it.manga.chapters, it.manga.chapters))
            }
        }

        return builder
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
