package com.gnoemes.shikimori.data.repository.anime.converter

import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.common.data.RolesResponse

interface AnimeDetailsResponseConverter {

    fun convertResponse(details: AnimeDetailsResponse, characters: List<RolesResponse>): AnimeDetails
}