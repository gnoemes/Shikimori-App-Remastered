package com.gnoemes.shikimori.entity.roles.domain

import com.gnoemes.shikimori.entity.common.domain.Image
import org.joda.time.DateTime

data class PersonDetails(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val nameJp: String?,
        val image: Image,
        val url: String,
        val jobTitle: String?,
        val birthDay: DateTime?,
        val works: List<Work>,
        val characters: List<Character>,
        val roles: List<List<String?>?>,
        val topicId: Long,
        val type: PersonType,
        val favoriteType: PersonType
)