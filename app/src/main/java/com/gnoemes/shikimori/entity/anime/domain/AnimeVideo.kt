package com.gnoemes.shikimori.entity.anime.domain

data class AnimeVideo(
        val id: Long,
        val name: String?,
        val url: String,
        val type: AnimeVideoType,
        val hosting: String?
)