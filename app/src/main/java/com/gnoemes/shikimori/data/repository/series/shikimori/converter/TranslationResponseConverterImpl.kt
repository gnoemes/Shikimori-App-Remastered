package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.domain.Translation
import javax.inject.Inject

class TranslationResponseConverterImpl @Inject constructor() : TranslationResponseConverter {

    override fun apply(t: List<TranslationResponse>): List<Translation> =
            t.map { convertResponse(it) }

    private fun convertResponse(it: TranslationResponse): Translation {
        return Translation(
                it.animeId,
                it.episodeId,
                it.id,
                it.type,
                it.quality,
                it.hosting,
                it.author,
                it.episodesSize,
                it.webPlayerUrl
        )
    }
}