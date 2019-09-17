package com.gnoemes.shikimori.entity.chronology

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.RelationType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class ChronologyItem(
        val id: Long,
        val rateId : Long?,
        val item: LinkedContent,
        val relation: RelationType,
        val rateStatus: RateStatus?
)