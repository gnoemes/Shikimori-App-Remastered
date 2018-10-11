package com.gnoemes.shikimori.data.repository.manga.converter

import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails

interface MangaDetailsResponseConverter {

    fun convertResponse(manga: MangaDetailsResponse, characters: List<RolesResponse>): MangaDetails

}