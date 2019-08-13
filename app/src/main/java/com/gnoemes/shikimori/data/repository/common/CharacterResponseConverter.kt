package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.domain.Character
import io.reactivex.functions.Function

interface CharacterResponseConverter : Function<List<CharacterResponse?>, List<Character>> {
    fun convertResponse(it: CharacterResponse?): Character?
}