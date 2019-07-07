package com.gnoemes.shikimori.entity.user.domain

data class UserStats(
        val animeStatuses: List<Status>?,
        val mangaStatuses: List<Status>?,
        val scores : UserStat,
        val types : UserStat,
        val ratings : UserStat,
        val hasAnime: Boolean,
        val hasManga: Boolean
)