package com.gnoemes.shikimori.entity.user.domain

data class Favorite(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val image: String,
        val url: String,
        val type: FavoriteType
)