package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class FrameItem(
        val image: Image,
        val name : String?,
        val typeText: CharSequence?,
        val raw : Any
)