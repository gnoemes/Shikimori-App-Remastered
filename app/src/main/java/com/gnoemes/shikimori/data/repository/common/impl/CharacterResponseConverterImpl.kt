package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class CharacterResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : CharacterResponseConverter {

    override fun apply(list: List<CharacterResponse?>): List<Character> =
            list.map { convertResponse(it)!! }

    override fun convertRoles(roles: List<RolesResponse?>): List<Character> =
            roles.asSequence()
                    .filter { it?.character != null }
                    .sortedBy { it?.character?.nameRu }
                    .sortedByDescending { it?.roles?.contains("Main") }
                    .map { convertResponse(it?.character)!! }.toList()

    override fun convertResponse(it: CharacterResponse?): Character? {
        if (it == null) {
            return null
        }

        return Character(
                it.id,
                it.name,
                it.nameRu,
                imageConverter.convertResponse(it.image),
                it.url.appendHostIfNeed()
        )
    }


}