package com.gnoemes.shikimori.data.repository.manga.converter

import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class MangaDetailsResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter,
        private val genreConverter: GenreResponseConverter,
        private val rateConverter: RateResponseConverter
) : MangaDetailsResponseConverter {

    override fun apply(t: MangaDetailsResponse): MangaDetails = MangaDetails(
            t.id,
            t.name,
            t.nameRu,
            imageConverter.convertResponse(t.image),
            t.url.appendHostIfNeed(),
            t.type,
            t.status,
            t.volumes,
            t.chapters,
            t.dateAired,
            t.dateReleased,
            t.score,
            t.description,
            t.descriptionHtml,
            t.favoured,
            t.topicId,
            genreConverter.apply(t.genres),
            rateConverter.convertUserRateResponse(t.id, t.userRate)
    )
}