package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.PersonResponseConverter
import com.gnoemes.shikimori.data.repository.common.RolesResponseConverter
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.utils.nullIfEmpty
import javax.inject.Inject

class RolesResponseConverterImpl @Inject constructor(
        private val characterConverter: CharacterResponseConverter,
        private val personConverter: PersonResponseConverter
) : RolesResponseConverter {

    override fun apply(t: List<RolesResponse>): Roles {
        val characters = t
                .asSequence()
                .filter { it.character != null }
                .sortedBy { it.character?.nameRu.nullIfEmpty() ?: it.character?.name }
                .sortedByDescending { it.roles.contains("Main") }
                .mapNotNull { characterConverter.convertResponse(it.character) }
                .toList()

        val persons = t
                .asSequence()
                .filter { it.person != null }
                .map { Pair(personConverter.convertResponse(it.person)!!, it.roles) }
                .toList()

        return Roles(characters, persons)
    }
}