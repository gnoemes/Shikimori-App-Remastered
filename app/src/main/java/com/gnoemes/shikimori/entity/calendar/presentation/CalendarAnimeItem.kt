package com.gnoemes.shikimori.entity.calendar.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class CalendarAnimeItem(
        val id: Long,
        val name: String,
        val image: Image,
        val isLast: Boolean,
        val description: CharSequence,
        val status: RateStatus?
)