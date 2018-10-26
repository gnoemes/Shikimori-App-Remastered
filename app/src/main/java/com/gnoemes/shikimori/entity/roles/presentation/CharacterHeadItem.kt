package com.gnoemes.shikimori.entity.roles.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class CharacterHeadItem(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val nameAlt: String?,
        val image: Image,
        val url: String,
        val nameJp: String?,
        val description: String?,
        val descriptionSource: String?
)