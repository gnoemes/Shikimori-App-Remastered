package com.gnoemes.shikimori.domain.search

import android.text.TextUtils
import androidx.collection.ArrayMap
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Single
import javax.inject.Inject

//TODO settings for 18+
class SearchQueryBuilderImpl @Inject constructor() : SearchQueryBuilder {

    companion object {
        private const val DIVIDER = ","
    }

    override fun createQueryFromFilters(filters: Map<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (filters.isNullOrEmpty()) return Single.fromCallable { getDefaultRequest(page, limit) }

        filters.forEach { entry -> queryMap[entry.key] = convertFilters(entry.value) }

        queryMap[SearchConstants.PAGE] = page.toString()
        queryMap[SearchConstants.LIMIT] = limit.toString()

        return Single.just(queryMap)
    }

    override fun createQueryFromIds(ids: MutableCollection<Long>, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            //TODO wtf? check and refactor when is time for use
            val builder = StringBuilder()
            ids.forEach { builder.append(it).append(DIVIDER) }
            builder.deleteCharAt(builder.length - 1)

            queryMap[SearchConstants.IDS] = ids.toString()
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

//            queryMap[SearchConstants.CENSORED] = true.toString()
        }
        return Single.just(queryMap)
    }

    override fun createMyListQueryFromIds(ids: MutableCollection<Long>, status: RateStatus, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            //TODO wtf? check and refactor when is time for use
            val builder = StringBuilder()
            ids.forEach { builder.append(it).append(DIVIDER) }
            builder.deleteCharAt(builder.length - 1)

            queryMap[SearchConstants.IDS] = ids.toString()
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.RATE] = status.status
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

    private fun convertFilters(values: MutableList<FilterItem>): String {
        return convertQuery(values.mapNotNull { it.value })
    }

    private fun convertQuery(values: List<String>): String {
        return if (!values.isNullOrEmpty()) {
            val builder = StringBuilder()
            values.forEach { builder.append(it).append(DIVIDER) }
            builder.replace(builder.lastIndexOf(DIVIDER), builder.length, "")
            builder.toString()
        } else ""
    }
}