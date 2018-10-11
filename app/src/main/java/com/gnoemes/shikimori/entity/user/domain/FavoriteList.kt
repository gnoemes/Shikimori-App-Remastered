package com.gnoemes.shikimori.entity.user.domain

data class FavoriteList(
        val animes: List<Favorite>,
        val mangas: List<Favorite>,
        val characters: List<Favorite>,
        val people: List<Favorite>,
        val mangakas: List<Favorite>,
        val seyu: List<Favorite>,
        val producers: List<Favorite>,
        val all: List<Favorite>
)