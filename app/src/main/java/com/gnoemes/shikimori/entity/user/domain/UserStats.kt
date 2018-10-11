package com.gnoemes.shikimori.entity.user.domain

data class UserStats(
        val animeStatuses: List<Status>?,
        val mangaStatuses: List<Status>?,
        val hasAnime: Boolean,
        val hasManga: Boolean
)