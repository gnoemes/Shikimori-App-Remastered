package com.gnoemes.shikimori.domain.search

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import io.reactivex.Single

interface SearchInteractor {

    fun loadAnimeList(page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Anime>>

    fun loadMangaList(page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Manga>>

    fun loadRanobeList(page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Manga>>

    fun loadCharacterListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int = 0, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Character>>

    fun loadPersonListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int = 0, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Person>>

    fun loadAnimeListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Anime>>

    fun loadMangaListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Manga>>

    fun loadRanobeListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Manga>>
}