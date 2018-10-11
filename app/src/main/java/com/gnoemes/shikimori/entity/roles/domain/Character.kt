package com.gnoemes.shikimori.entity.roles.domain

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type

data class Character(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val image: Image,
        val url: String
) : LinkedContent(id, Type.CHARACTER, image.original, name)