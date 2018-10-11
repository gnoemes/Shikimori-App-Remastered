package com.gnoemes.shikimori.data.repository.search

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import io.reactivex.Single

interface SearchRepository {

    fun getAnimeList(queryMap: Map<String, String>): Single<List<Anime>>

    fun getMangaList(queryMap: Map<String, String>): Single<List<Manga>>

    fun getRanobeList(queryMap: Map<String, String>): Single<List<Manga>>

    fun getCharacterList(queryMap: Map<String, String>): Single<List<Character>>

    fun getPersonList(queryMap: Map<String, String>): Single<List<Person>>
}