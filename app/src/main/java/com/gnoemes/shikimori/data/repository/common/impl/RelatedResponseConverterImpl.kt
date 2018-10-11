package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.data.repository.common.RelatedResponseConverter
import com.gnoemes.shikimori.entity.common.data.RelatedResponse
import com.gnoemes.shikimori.entity.common.domain.Related
import javax.inject.Inject

class RelatedResponseConverterImpl @Inject constructor(
        private val animeConverter: AnimeResponseConverter,
        private val mangaConverter: MangaResponseConverter
) : RelatedResponseConverter {

    override fun apply(t: List<RelatedResponse>): List<Related> =
            t.map { convertResponse(it) }

    private fun convertResponse(it: RelatedResponse): Related = Related(
            it.relation,
            it.relationRu,
            animeConverter.convertResponse(it.anime),
            mangaConverter.convertResponse(it.manga)
    )
}