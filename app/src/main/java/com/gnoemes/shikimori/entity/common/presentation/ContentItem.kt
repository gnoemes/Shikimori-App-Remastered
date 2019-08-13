package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class ContentItem(
        val name: String,
        val image: Image,
        val description: CharSequence?,
        val raw : Any
)