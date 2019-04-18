package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import org.jsoup.nodes.Document

interface DocumentParsingConverter {

    fun convertEpisodes(it : Document, animeId : Long) : List<EpisodeResponse>

    fun convertTranslations(it : Document) : List<TranslationResponse>
}