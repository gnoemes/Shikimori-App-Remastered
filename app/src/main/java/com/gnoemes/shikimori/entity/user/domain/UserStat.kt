package com.gnoemes.shikimori.entity.user.domain

data class UserStat(
        val anime : List<Statistic>,
        val manga : List<Statistic>?
)