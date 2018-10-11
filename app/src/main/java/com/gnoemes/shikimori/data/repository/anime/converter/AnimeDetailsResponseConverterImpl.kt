package com.gnoemes.shikimori.data.repository.anime.converter

import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.data.AnimeVideoResponse
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class AnimeDetailsResponseConverterImpl @Inject constructor(
        private val charactersConverter: CharacterResponseConverter,
        private val imageConverter: ImageResponseConverter,
        private val genreConverter: GenreResponseConverter,
        private val rateResponseConverter: RateResponseConverter
) : AnimeDetailsResponseConverter {

    override fun convertResponse(details: AnimeDetailsResponse, characters: List<RolesResponse>): AnimeDetails = AnimeDetails(
            details.id,
            details.name,
            details.nameRu,
            imageConverter.convertResponse(details.image),
            details.url.appendHostIfNeed(),
            details.type,
            details.status,
            details.episodes,
            details.episodesAired,
            details.dateAired,
            details.dateReleased,
            details.namesEnglish,
            details.namesJapanese,
            details.score,
            details.duration,
            details.description,
            details.descriptionHtml,
            details.favoured,
            details.topicId,
            genreConverter.apply(details.genres),
            rateResponseConverter.convertUserRateResponse(details.userRate),
            convertVideos(details.videoResponses),
            charactersConverter.convertRoles(characters)
    )

    private fun convertVideos(videoResponses: List<AnimeVideoResponse>?): List<AnimeVideo>? {
        if (videoResponses == null) {
            return null
        }

        return videoResponses.map { AnimeVideo(it.id, it.name, it.url, it.type, it.hosting) }
    }
}