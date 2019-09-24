package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type

data class InfoClickableItem(
        val id: Long,
        val type: Type,
        val description: CharSequence,
        val image : Image?,
        val category: CharSequence
)