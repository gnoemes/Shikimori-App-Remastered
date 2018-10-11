package com.gnoemes.shikimori.domain.search

import android.support.v4.util.ArrayMap
import android.text.TextUtils
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Single
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

//TODO settings for 18+
class SearchQueryBuilderImpl @Inject constructor() : SearchQueryBuilder {

    companion object {
        private const val DIVIDER = ","
        private const val SEASON_DIVIDER = "_"
    }

    override fun createQueryFromFilters(filters: Map<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (filters == null || filters.isEmpty()) {
            return Single.fromCallable { getDefaultRequest(page, limit) }
        }

        val genreQuery = ArrayList<FilterItem>()
        val typeQuery = ArrayList<FilterItem>()
        val statusQuery = ArrayList<FilterItem>()
        val orderQuery = ArrayList<FilterItem>()
        val seasonQuery = ArrayList<FilterItem>()
        val durationQuery = ArrayList<FilterItem>()
        val searchQuery = ArrayList<FilterItem>()

        filters.flatMap { entry -> entry.value }
                .forEach { item ->
                    when (item.action) {
                        SearchConstants.GENRE -> genreQuery.add(item)
                        SearchConstants.TYPE -> typeQuery.add(item)
                        SearchConstants.STATUS -> statusQuery.add(item)
                        SearchConstants.SEASON -> seasonQuery.add(item)
                        SearchConstants.ORDER -> orderQuery.add(item)
                        SearchConstants.DURATION -> durationQuery.add(item)
                        SearchConstants.SEARCH -> searchQuery.add(item)
                    }
                }

        putToQuery(queryMap, genreQuery)
        putToQuery(queryMap, typeQuery)
        putToQuery(queryMap, statusQuery)
        putToQuery(queryMap, orderQuery)
        putSeasonToQuery(queryMap, seasonQuery)
        putToQuery(queryMap, durationQuery)
        putToQuery(queryMap, searchQuery)

        queryMap[SearchConstants.PAGE] = page.toString()
        queryMap[SearchConstants.LIMIT] = limit.toString()

        queryMap[SearchConstants.CENSORED] = true.toString()



        return Single.just(queryMap)
    }

    override fun createQueryFromIds(ids: MutableCollection<Long>, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            val builder = StringBuilder()
            ids.forEach { builder.append(it).append(DIVIDER) }
            builder.deleteCharAt(builder.length - 1)

            queryMap[SearchConstants.IDS] = ids.toString()
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.CENSORED] = true.toString()
        }
        return Single.just(queryMap)
    }

    override fun createMyListQueryFromIds(ids: MutableCollection<Long>, status: RateStatus, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            val builder = StringBuilder()
            ids.forEach { builder.append(it).append(DIVIDER) }
            builder.deleteCharAt(builder.length - 1)

            queryMap[SearchConstants.IDS] = ids.toString()
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.MY_LIST] = status.status
        }
        return Single.just(queryMap)
    }

    private fun getDefaultRequest(page: Int, limit: Int): MutableMap<String, String> {
        val queryMap = ArrayMap<String, String>()
        queryMap[SearchConstants.PAGE] = page.toString()
        queryMap[SearchConstants.LIMIT] = limit.toString()
        queryMap[SearchConstants.ORDER] = SearchConstants.ORDER_BY.POPULARITY.toString()
        return queryMap
    }

    private fun putToQuery(map: MutableMap<String, String>, filters: List<FilterItem>?) {
        if (filters != null && !filters.isEmpty()) {
            val genreString = StringBuilder()
            for (item in filters) {
                genreString.append(item.value)
                        .append(DIVIDER)
            }

            genreString.deleteCharAt(genreString.length - 1)
            map[filters[0].action] = genreString.toString()
        }
    }

    private fun putSeasonToQuery(map: MutableMap<String, String>, filters: List<FilterItem>?) {
        if (filters != null && !filters.isEmpty()) {
            val genreString = StringBuilder()
            for (item in filters) {
                val season = item.value
                if (!Pattern.matches("\\d+", season)) {
                    genreString.append(season)
                    genreString.append(SEASON_DIVIDER)
                } else {
                    genreString.append(season)
                    genreString.append(DIVIDER)
                }
            }

            genreString.deleteCharAt(genreString.length - 1)
            map[filters[0].action] = genreString.toString()
        }
    }
}