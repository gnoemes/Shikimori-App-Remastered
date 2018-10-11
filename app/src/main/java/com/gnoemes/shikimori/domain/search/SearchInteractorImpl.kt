package com.gnoemes.shikimori.domain.search

import com.gnoemes.shikimori.data.repository.search.SearchRepository
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
        private val repository: SearchRepository,
        private val queryBuilder: SearchQueryBuilder
) : SearchInteractor {

    override fun loadAnimeList(page: Int, limit: Int): Single<List<Anime>> =
            queryBuilder.createQueryFromFilters(null, page, limit)
                    .flatMap { repository.getAnimeList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadMangaList(page: Int, limit: Int): Single<List<Manga>> =
            queryBuilder.createQueryFromFilters(null, page, limit)
                    .flatMap { repository.getMangaList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadRanobeList(page: Int, limit: Int): Single<List<Manga>> =
            queryBuilder.createQueryFromFilters(null, page, limit)
                    .flatMap { repository.getMangaList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadCharacterListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<List<Character>> =
            queryBuilder.createQueryFromFilters(filters, page, limit)
                    .flatMap { repository.getCharacterList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadPersonListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<List<Person>> =
            queryBuilder.createQueryFromFilters(filters, page, limit)
                    .flatMap { repository.getPersonList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadAnimeListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<List<Anime>> =
            queryBuilder.createQueryFromFilters(filters, page, limit)
                    .flatMap { repository.getAnimeList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadMangaListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<List<Manga>> =
            queryBuilder.createQueryFromFilters(filters, page, limit)
                    .flatMap { repository.getMangaList(it) }
                    .applyErrorHandlerAndSchedulers()

    override fun loadRanobeListWithFilters(filters: HashMap<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<List<Manga>> =
            queryBuilder.createQueryFromFilters(filters, page, limit)
                    .flatMap { repository.getRanobeList(it) }
                    .applyErrorHandlerAndSchedulers()

}