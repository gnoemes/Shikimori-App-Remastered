package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.utils.appendHostIfNeed
import com.gnoemes.shikimori.utils.nullIfEmpty
import javax.inject.Inject

class AnimeResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : AnimeResponseConverter {
    override fun apply(list: List<AnimeResponse?>): List<Anime> = list.map { convertResponse(it)!! }

    override fun convertResponse(response: AnimeResponse?): Anime? {
        if (response == null) {
            return null
        }

        return Anime(
                response.id,
                response.name.trim(),
                response.nameRu?.trim().nullIfEmpty(),
                imageConverter.convertResponse(response.image),
                response.url.appendHostIfNeed(),
                response.type,
                response.score,
                response.status,
                response.episodes,
                response.episodesAired,
                response.dateAired,
                response.dateReleased
        )
    }
}