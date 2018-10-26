package com.gnoemes.shikimori.entity.roles.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class PersonHeadItem(
        val name: String,
        val nameRu: String?,
        val nameJp: String?,
        val image: Image,
        val jobTitle: String?,
        val birthDay: String?
)