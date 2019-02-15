package com.gnoemes.shikimori.presentation.presenter.favorites.converter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.domain.Favorite
import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import com.gnoemes.shikimori.entity.user.domain.FavoriteType
import com.gnoemes.shikimori.entity.user.presentation.FavoriteViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserContentItem
import javax.inject.Inject

class FavoriteViewModelConverterImpl @Inject constructor() : FavoriteViewModelConverter {

    override fun apply(t: FavoriteList): List<FavoriteViewModel> {
        val items = mutableListOf<FavoriteViewModel>()

        items.apply {
            add(FavoriteViewModel(FavoriteType.ANIME, t.animes.map { convertFavorite(it, Type.ANIME) }))
            add(FavoriteViewModel(FavoriteType.MANGA, t.mangas.map { convertFavorite(it, Type.MANGA) }))
            add(FavoriteViewModel(FavoriteType.CHARACTERS, t.characters.map { convertFavorite(it, Type.CHARACTER) }))
            add(FavoriteViewModel(FavoriteType.SEYU, t.seyu.map { convertFavorite(it, Type.PERSON) }))
            add(FavoriteViewModel(FavoriteType.PRODUCERS, t.producers.map { convertFavorite(it, Type.PERSON) }))
            add(FavoriteViewModel(FavoriteType.MANGAKAS, t.mangakas.map { convertFavorite(it, Type.PERSON) }))
            add(FavoriteViewModel(FavoriteType.PEOPLE, t.people.map { convertFavorite(it, Type.PERSON) }))
        }

        return items
    }

    private fun convertFavorite(it: Favorite, type: Type): UserContentItem =
            UserContentItem(
                    it.id,
                    type,
                    it.image
            )
}