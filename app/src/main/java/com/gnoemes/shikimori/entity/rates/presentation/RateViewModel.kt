package com.gnoemes.shikimori.entity.rates.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.Rate

data class RateViewModel(
        val id: Long,
        val contentId: Long,
        val type: Type,
        val image: Image,
        val name: String,
        val description: CharSequence,
        val rating: String,
        val progress: String,
        val rawRate: Rate
)