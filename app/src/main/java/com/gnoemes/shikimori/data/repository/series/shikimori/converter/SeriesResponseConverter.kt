package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Series
import org.jsoup.nodes.Document

interface SeriesResponseConverter {
    fun convertResponse(animeId: Long, doc: Document): Series
}