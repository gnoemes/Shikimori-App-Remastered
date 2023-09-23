package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.utils.appendHostIfNeed
import com.gnoemes.shikimori.utils.nullIfEmpty
import javax.inject.Inject

class CharacterResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : CharacterResponseConverter {

    override fun apply(list: List<CharacterResponse?>): List<Character> =
            list.mapNotNull { convertResponse(it) }

    override fun convertResponse(it: CharacterResponse?): Character? {
        if (it == null) {
            return null
        }

        return Character(
                it.id,
                it.name.trim(),
                it.nameRu?.trim().nullIfEmpty(),
                imageConverter.convertResponse(it.image),
                it.url.appendHostIfNeed()
        )
    }


}