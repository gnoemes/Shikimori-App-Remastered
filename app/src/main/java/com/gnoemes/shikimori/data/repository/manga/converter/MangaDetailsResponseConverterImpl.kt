package com.gnoemes.shikimori.data.repository.manga.converter

import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import javax.inject.Inject

class MangaDetailsResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter,
        private val genreConverter: GenreResponseConverter,
        private val rateConverter: RateResponseConverter,
        private val charactersConverter: CharacterResponseConverter
) : MangaDetailsResponseConverter {
    override fun convertResponse(manga: MangaDetailsResponse, characters: List<RolesResponse>): MangaDetails =
            MangaDetails(
                    manga.id,
                    manga.name,
                    manga.nameRu,
                    imageConverter.convertResponse(manga.image),
                    manga.url,
                    manga.type,
                    manga.status,
                    manga.volumes,
                    manga.chapters,
                    manga.dateAired,
                    manga.dateReleased,
                    manga.score,
                    manga.description,
                    manga.descriptionHtml,
                    manga.favoured,
                    manga.topicId,
                    genreConverter.apply(manga.genres),
                    charactersConverter.convertRoles(characters),
                    rateConverter.convertUserRateResponse(manga.id, manga.userRate)
            )
}