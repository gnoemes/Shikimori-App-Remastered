package com.gnoemes.shikimori.entity.search.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type

data class SearchItem(
        val id: Long,
        val type: Type,
        val name: String,
        val image: Image,
        val typeText: String?
)