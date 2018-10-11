package com.gnoemes.shikimori.data.repository.roles.converter

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.data.repository.common.PersonResponseConverter
import com.gnoemes.shikimori.entity.roles.data.CharacterDetailsResponse
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class CharacterDetailsResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter,
        private val animeConverter: AnimeResponseConverter,
        private val mangaConverter: MangaResponseConverter,
        private val personConverter: PersonResponseConverter
) : CharacterDetailsResponseConverter {

    override fun convertResponsse(it: CharacterDetailsResponse): CharacterDetails = CharacterDetails(
            it.id,
            it.name,
            it.nameRu,
            imageConverter.convertResponse(it.image),
            it.url.appendHostIfNeed(),
            it.nameAlt,
            it.nameJp,
            it.description,
            it.descriptionSource,
            personConverter.apply(it.seyu ?: emptyList()),
            animeConverter.apply(it.animes),
            mangaConverter.apply(it.mangas)
    )
}