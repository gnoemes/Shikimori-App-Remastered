package com.gnoemes.shikimori.presentation.presenter.series.translations.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.utils.Utils
import java.net.URLEncoder
import javax.inject.Inject

class TranslationsViewModelConverterImpl @Inject constructor(
        private val context: Context
) : TranslationsViewModelConverter {

    private val unknownAuthor by lazy { context.getString(R.string.translation_unknown_author) }

    override fun convertTranslations(translations: List<Translation>, setting: TranslationSetting?): List<TranslationViewModel> {


        return translations
                .mapIndexed { index, translation -> if (translation.author.isEmpty()) translation.copy(author = "$unknownAuthor $index") else translation }
                .groupBy { it.author.trim() }
                .mapNotNull { convertTranslation(it, setting) }
    }


    private fun convertTranslation(it: Map.Entry<String, List<Translation>>, setting: TranslationSetting?): TranslationViewModel? {
        if (it.value.isEmpty()) return null

        val author = if (it.key.contains(unknownAuthor)) it.key.replace(Regex("\\d"), "") else it.key
        val builder = StringBuilder()

        fun appendDot(withSpace: Boolean = false) {
            val space = " "
            val dot = "•"
            builder.append(space.plus(dot))
            if (withSpace) builder.append(space)
        }

        val hasBd = it.value.find { it.quality == TranslationQuality.BD || it.quality == TranslationQuality.DVD } != null
        val isSameAuthor = author == setting?.lastAuthor
        val canBeDownloaded = it.value.find { Utils.isHostingSupports(it.hosting) } != null


        if (isSameAuthor) {
            builder.append("  ")
            if (!hasBd) {
                val lastAuthor = context.getString(R.string.translation_last_author)
                builder.append(lastAuthor)
            }
        }
        if (hasBd) {
            if (builder.isNotEmpty()) {
                builder.replace(0, 2, "")
                appendDot(true)
            }
            val blueRay = context.getString(R.string.translation_blue_ray)
            builder.append(blueRay)
        }

        if (!hasBd && !isSameAuthor) {
            if (builder.isNotEmpty()) appendDot(true)
            val text = context.getString(R.string.translation_sources)
            builder.append(text)
        }

        val description: CharSequence? = if (builder.isNotEmpty()) builder else null
        val videos = it.value.map { t -> convertVideo(t) }

        return TranslationViewModel(
                it.value.first().videoId,
                it.value.first().type,
                author,
                description,
                videos,
                isSameAuthor,
                canBeDownloaded,
                it.value.first().episodesSize
        )
    }

    override fun convertTrack(video: Video, track: Track): SeriesDownloadItem = SeriesDownloadItem(
            video.hosting.synonymType,
            track.quality.let { if (it.isBlank() || it == "unknown")  null else "${it}p"},
            track.url,
            video
    )

    private fun convertVideo(t: Translation): TranslationVideo {

        val simpleAuthor = URLEncoder.encode(t.author.replace(Regex("\\(.+\\)"), "").trim(), "utf-8")

        return TranslationVideo(
                t.videoId,
                t.animeId,
                t.episodeId,
                //TODO multi language
                "russian",
                if (t.author.contains(unknownAuthor)) "" else t.author,
                simpleAuthor,
                t.type,
                t.hosting,
                t.webPlayerUrl
        )
    }

}