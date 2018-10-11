package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.club.ClubResponseConverter
import com.gnoemes.shikimori.data.repository.common.*
import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import javax.inject.Inject

class LinkedContentResponseConverterImpl @Inject constructor(
        private val animeConverter: AnimeResponseConverter,
        private val mangaConverter: MangaResponseConverter,
        private val charactersConverter: CharacterResponseConverter,
        private val personConverter: PersonResponseConverter,
        private val clubConverter: ClubResponseConverter
) : LinkedContentResponseConverter {

    override fun convertResponse(it: LinkedContentResponse?): LinkedContent? =
            when (it) {
                is AnimeResponse -> animeConverter.convertResponse(it)
                is MangaResponse -> mangaConverter.convertResponse(it)
                is CharacterResponse -> charactersConverter.convertResponse(it)
                is PersonResponse -> personConverter.convertResponse(it)
                is ClubResponse -> clubConverter.convertResponse(it)
                else -> null
            }
}