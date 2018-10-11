package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class TranslationResponseConverterImpl @Inject constructor(
        private val seriesConverter: SeriesResponseConverter
) : TranslationResponseConverter {

    companion object {
        private const val ALL_QUERY = "div.video-variant-group~[data-kind=all]"
        private const val TRANSLATIONS_QUERY = "div.b-video_variant[data-video_id]"
        private const val VIDEO_ID_QUERY = "data-video_id"
        private const val REJECTED_QUERY = "rejected"
        private const val BROKEN_QUERY = "broken"
        private const val BANNED_QUERY = "banned_hosting"
        private const val QUALITY_QUERY = "video-quality"
        private const val TRANSLATION_TYPE_QUERY = "video-kind"
        private const val VIDEO_HOSTING_QUERY = "video-hosting"
        private const val AUTHOR_QUERY = "video-author"
    }

    override fun convertResponse(animeId: Long, episodeId: Int, doc: Document): List<Translation> {
        val episodeSize = seriesConverter.convertResponse(animeId, doc).episodesSize

        return doc.select(ALL_QUERY)
                .first()
                ?.select(TRANSLATIONS_QUERY)
                ?.map { convertTranslation(animeId, episodeId, it, episodeSize) }
                ?: emptyList()
    }

    private fun convertTranslation(animeId: Long, episodeId: Int, e: Element, episodeSize: Int): Translation {
        val videoId = e.attr(VIDEO_ID_QUERY).toLong()
        val isValid = e.getElementsByClass(REJECTED_QUERY).first() == null &&
                e.getElementsByClass(BROKEN_QUERY).first() == null &&
                e.getElementsByClass(BANNED_QUERY).first() == null

        val quality = e.getElementsByClass(QUALITY_QUERY)
                .toString()
                .replace("(<span class=\")", "")
                .replace("(\"></span>)", "")
                .replaceFirst("video-quality", "")
                .let { quality ->
                    TranslationQuality.values()
                            .find { it.equalQuality(quality.trim().toLowerCase()) }
                            ?: TranslationQuality.TV
                }

        val type = e.getElementsByClass(TRANSLATION_TYPE_QUERY)
                .text()
                .let { type ->
                    TranslationType.values().find { it.isEqualType(type.trim().toLowerCase()) }
                            ?: TranslationType.ALL
                }

        val hosting = e.getElementsByClass(VIDEO_HOSTING_QUERY)
                .text()
                .let { hosting ->
                    VideoHosting.values().find { it.isEqualType(hosting) } ?: VideoHosting.UNKNOWN
                }

        val author = e.getElementsByClass(AUTHOR_QUERY).text()

        return Translation(animeId, episodeId, videoId, type, quality, hosting, author, isValid, episodeSize)
    }
}