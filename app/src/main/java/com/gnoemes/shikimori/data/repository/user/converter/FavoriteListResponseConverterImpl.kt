package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.FavoriteListResponse
import com.gnoemes.shikimori.entity.user.data.FavoriteResponse
import com.gnoemes.shikimori.entity.user.domain.Favorite
import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import com.gnoemes.shikimori.entity.user.domain.FavoriteType
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class FavoriteListResponseConverterImpl @Inject constructor() : FavoriteListResponseConverter {
    override fun apply(t: FavoriteListResponse): FavoriteList {
        val all = mutableListOf<Favorite>()
        val animes = t.animes.map { convertFavorite(it, FavoriteType.ANIME) }
        val mangas = t.mangas.map { convertFavorite(it, FavoriteType.MANGA) }
        val characters = t.characters.map { convertFavorite(it, FavoriteType.CHARACTERS) }
        val people = t.people.map { convertFavorite(it, FavoriteType.PEOPLE) }
        val mangakas = t.mangakas.map { convertFavorite(it, FavoriteType.MANGAKAS) }
        val seyu = t.seyu.map { convertFavorite(it, FavoriteType.SEYU) }
        val producers = t.producers.map { convertFavorite(it, FavoriteType.PRODUCERS) }

        all.apply {
            addAll(animes)
            addAll(mangas)
            addAll(characters)
            addAll(people)
            addAll(mangakas)
            addAll(seyu)
            addAll(producers)
        }

        return FavoriteList(animes, mangas, characters, people, mangakas, seyu, producers, all)
    }

    private fun convertFavorite(it: FavoriteResponse, type: FavoriteType): Favorite = Favorite(
            it.id,
            it.name,
            it.nameRu,
            it.image.appendHostIfNeed().replace("x64", "original"),
            it.url.appendHostIfNeed(),
            type)
}