package com.gnoemes.shikimori.entity.chronology

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus


data class ChronologyViewModel(
        val id: Long,
        val rateId: Long,
        val type: Type,
        val title: String,
        val description: CharSequence,
        val image: Image,
        val relation: String,
        val status: RateStatus?,
        val isGuest: Boolean
)