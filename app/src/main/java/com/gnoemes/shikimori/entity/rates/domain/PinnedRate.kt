package com.gnoemes.shikimori.entity.rates.domain

import com.gnoemes.shikimori.entity.common.domain.Type

data class PinnedRate(
        val id: Long,
        val type: Type,
        val status: RateStatus,
        val order: Int
)