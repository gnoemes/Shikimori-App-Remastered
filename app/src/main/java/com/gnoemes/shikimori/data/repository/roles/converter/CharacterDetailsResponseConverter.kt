package com.gnoemes.shikimori.data.repository.roles.converter

import com.gnoemes.shikimori.entity.roles.data.CharacterDetailsResponse
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails

interface CharacterDetailsResponseConverter {

    fun convertResponsse(it: CharacterDetailsResponse): CharacterDetails
}