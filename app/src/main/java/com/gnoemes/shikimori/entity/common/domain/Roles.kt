package com.gnoemes.shikimori.entity.common.domain

import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person

data class Roles(
        val characters: List<Character>,
        val persons: List<Pair<Person, List<String>>>
)