package com.gnoemes.shikimori.entity.similar.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class SimilarViewModel(
        val id: Long,
        val rateId : Long,
        val type: Type,
        val title: String,
        val description: CharSequence,
        val image: Image,
        val score : Double?,
        val status: RateStatus?,
        val isGuest : Boolean
)