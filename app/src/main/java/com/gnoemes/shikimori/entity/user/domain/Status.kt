package com.gnoemes.shikimori.entity.user.domain

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class Status(
        val id: Long,
        val name: RateStatus,
        val size: Int,
        val type: Type
)