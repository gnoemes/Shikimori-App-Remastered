package com.gnoemes.shikimori.entity.user.presentation

data class UserRateViewModel(
        val isAnime: Boolean,
        val rates: Map<RateProgressStatus, Int>,
        val averageScore : Float,
        val scores : List<UserStatisticItem>,
        val types : List<UserStatisticItem>,
        val ratings : List<UserStatisticItem>
)