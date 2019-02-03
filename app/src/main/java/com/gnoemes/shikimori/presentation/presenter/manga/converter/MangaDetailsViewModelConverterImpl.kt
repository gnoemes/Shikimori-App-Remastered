package com.gnoemes.shikimori.presentation.presenter.manga.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsOptionsItem
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.nullIfEmpty
import com.gnoemes.shikimori.utils.unknownIfZero
import javax.inject.Inject

class MangaDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val settings: SettingsSource,
        private val converter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : MangaDetailsViewModelConverter {

    override fun convertHead(it: MangaDetails): DetailsHeadItem {
        val name = if (!settings.isRussianNaming) it.name else it.nameRu.nullIfEmpty() ?: it.name
        val nameSecond = if (!settings.isRussianNaming) it.nameRu ?: it.name else it.name

        val season = converter.convertAnimeSeasonToString(it.dateAired)
        val type = convertType(it.type, it.volumes, it.chapters)
        val status = convertStatus(it.status)

        return DetailsHeadItem(
                Type.ANIME,
                name,
                nameSecond,
                it.image,
                type,
                season,
                status,
                null,
                it.score,
                it.genres,
                null
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

    private fun convertType(type: MangaType, volumes: Int, chapters: Int): String {
        val typeText = getLocalizedType(type)
        val format = context.getString(R.string.type_pattern_manga)
        return String.format(format, typeText, volumes.unknownIfZero(), chapters.unknownIfZero())
    }

    private fun getLocalizedType(type: MangaType): String {
        return when (type) {
            MangaType.MANGA -> context.getString(R.string.type_manga_translatable)
            MangaType.DOUJIN -> context.getString(R.string.type_doujin_translatable)
            MangaType.MANHUA -> context.getString(R.string.type_manhua_translatable)
            MangaType.MANHWA -> context.getString(R.string.type_manhwa_translatable)
            MangaType.NOVEL -> context.getString(R.string.type_novel_translatable)
            MangaType.ONE_SHOT -> context.getString(R.string.type_one_shot_translatable)
            MangaType.UNKNOWN -> ""
        }
    }

    override fun convertOptions(it: MangaDetails, isGuest: Boolean): DetailsOptionsItem {
        return DetailsOptionsItem(
                it.userRate?.status,
                false,
                isGuest,
                context.getString(R.string.details_read_online),
                context.getString(R.string.common_chronology_read
                )
        )
    }

    override fun convertDescriptionItem(description: String?): DetailsDescriptionItem {
        val processedText = description?.let { textProcessor.process(it) }
        return DetailsDescriptionItem(processedText)
    }
}