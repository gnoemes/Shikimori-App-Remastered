package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Series
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class SeriesResponseConverterImpl @Inject constructor() : SeriesResponseConverter {

    companion object {
        private const val ERRORS_QUERY = "div.b-errors p"
        private const val EPISODES_QUERY = "div.c-anime_video_episodes>[data-episode],div.b-show_more-more>[data-episode]"
        private const val EPISODE_ID_QUERY = "data-episode"
        private const val EPISODE_TRANSLATIONS_QUERY = "episode-kinds"
        private const val EPISODE_HOSTINGS_QUERY = "episode-hostings"
    }

    override fun convertResponse(animeId: Long, doc: Document): Series {

        val error = doc.select(ERRORS_QUERY).first()?.text()

        val episodes = doc.select(EPISODES_QUERY).map { convertEpisode(animeId, it) }

        return Series(episodes, errorMessage = error)
    }

    private fun convertEpisode(animeId: Long, e: Element): Episode {
        val id = e.attr(EPISODE_ID_QUERY).toInt()
        val translations = e.getElementsByClass(EPISODE_TRANSLATIONS_QUERY)
                .text()
                .replace(" ", "")
                .split(",")
                .asSequence()
                .map { type -> TranslationType.values().first { it.isEqualType(type) } }
                .toSet()
                .toList()
        val rawHostings = e.getElementsByClass(EPISODE_HOSTINGS_QUERY).text()
        val videoHostings = rawHostings.replace(" ", "")
                .split(",")
                .map { hosting -> VideoHosting.values().first { it.isEqualType(hosting) } }

        return Episode(id, animeId, translations, videoHostings, rawHostings, false)
    }
}