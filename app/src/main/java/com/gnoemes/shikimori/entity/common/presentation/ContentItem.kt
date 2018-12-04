package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class ContentItem(
        val name: String,
        val typeText: String?,
        val image: Image,
        val description: String?,
        val raw : Any
)