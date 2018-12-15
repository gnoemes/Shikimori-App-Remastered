package com.gnoemes.shikimori.entity.roles.domain

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.manga.domain.Manga

data class CharacterDetails(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val image: Image,
        val url: String,
        val nameAlt: String?,
        val nameJp: String?,
        val description: String?,
        val descriptionSource: String?,
        val seyu: List<Person>,
        val animes: List<Anime>,
        val mangas: List<Manga>
)