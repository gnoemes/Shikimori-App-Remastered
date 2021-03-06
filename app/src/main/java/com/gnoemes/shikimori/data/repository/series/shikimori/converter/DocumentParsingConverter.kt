package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoRequest
import org.jsoup.nodes.Document

interface DocumentParsingConverter {

    fun convertEpisodes(it : Document, animeId : Long) : List<EpisodeResponse>

    fun convertTranslations(it: Document, animeId: Long, episodeId : Long, type: String) : List<TranslationResponse>

    fun convertVideoRequest(it: Document, animeId: Long, episodeId: Int): VideoRequest

    fun convertCookie(language: String, type: String, author: String, hosting: String): String
}