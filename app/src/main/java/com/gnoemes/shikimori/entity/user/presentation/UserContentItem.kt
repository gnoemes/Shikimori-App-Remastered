package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type

data class UserContentItem(
        val id: Long,
        val type: Type,
        val image: Image
)