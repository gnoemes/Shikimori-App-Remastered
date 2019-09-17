package com.gnoemes.shikimori.data.repository.search

import com.gnoemes.shikimori.data.network.AnimeApi
import com.gnoemes.shikimori.data.network.MangaApi
import com.gnoemes.shikimori.data.network.RanobeApi
import com.gnoemes.shikimori.data.network.RolesApi
import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.data.repository.common.PersonResponseConverter
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import io.reactivex.Single
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
        private val animesApi: AnimeApi,
        private val mangaApi: MangaApi,
        private val ranobeApi: RanobeApi,
        private val rolesApi: RolesApi,
        private val animeResponseConverter: AnimeResponseConverter,
        private val mangaResponseConverter: MangaResponseConverter,
        private val characterResponseConverter: CharacterResponseConverter,
        private val personResponseConverter: PersonResponseConverter
) : SearchRepository {

    override fun getAnimeList(queryMap: Map<String, String>): Single<List<Anime>> =
            animesApi.getList(queryMap)
                    .map(animeResponseConverter)

    override fun getMangaList(queryMap: Map<String, String>): Single<List<Manga>> =
            mangaApi.getList(queryMap)
                    .map(mangaResponseConverter)

    override fun getRanobeList(queryMap: Map<String, String>): Single<List<Manga>> =
            ranobeApi.getList(queryMap)
                    .map(mangaResponseConverter)

    override fun getCharacterList(queryMap: Map<String, String>): Single<List<Character>> =
            rolesApi.getCharacterList(queryMap)
                    .map(characterResponseConverter)

    override fun getPersonList(queryMap: Map<String, String>): Single<List<Person>> =
            rolesApi.getPersonList(queryMap)
                    .map(personResponseConverter)

    override fun getList(type: Type, queryMap: Map<String, String>): Single<List<LinkedContent>> =
            (when (type) {
                Type.ANIME -> getAnimeList(queryMap)
                Type.MANGA -> getMangaList(queryMap)
                Type.RANOBE -> getRanobeList(queryMap)
                Type.CHARACTER -> getCharacterList(queryMap)
                Type.PERSON -> getCharacterList(queryMap)
                else -> Single.error(IllegalArgumentException("$type search is not supported"))
            })
                    .map { it }

}