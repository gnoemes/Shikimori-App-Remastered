package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Translation
import org.jsoup.nodes.Document

interface TranslationResponseConverter {

    fun convertResponse(animeId: Long, episodeId: Int, doc: Document): List<Translation>
}