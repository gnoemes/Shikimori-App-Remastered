package com.gnoemes.shikimori.domain.search

import android.text.TextUtils
import androidx.collection.ArrayMap
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Single
import javax.inject.Inject

class SearchQueryBuilderImpl @Inject constructor(
        private val settingsSource: SettingsSource
) : SearchQueryBuilder {

    companion object {
        private const val DIVIDER = ","
    }

    override fun createQueryFromFilters(filters: Map<String, MutableList<FilterItem>>?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (filters.isNullOrEmpty()) return Single.fromCallable { getDefaultRequest(page, limit) }

        filters.forEach { entry -> queryMap[entry.key] = convertFilters(entry.value) }

        queryMap[SearchConstants.PAGE] = page.toString()
        queryMap[SearchConstants.LIMIT] = limit.toString()
        queryMap[SearchConstants.CENSORED] = (!settingsSource.allowR18Content).toString()

        return Single.just(queryMap)
    }

    override fun createQueryFromIds(ids: MutableCollection<Long>, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            val query = convertQuery(ids.map { it.toString() })

            queryMap[SearchConstants.IDS] = query
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.CENSORED] = (!settingsSource.allowR18Content).toString()
        }

        return Single.just(queryMap)
    }

    override fun createMyListQueryFromIds(ids: MutableCollection<Long>, status: RateStatus, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (ids.isNotEmpty()) {
            val query = convertQuery(ids.map { it.toString() })

            queryMap[SearchConstants.IDS] = query
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.RATE] = status.status
            queryMap[SearchConstants.CENSORED] = (!settingsSource.allowR18Content).toString()
        }
        return Single.just(queryMap)
    }

    override fun createQueryFromFranchise(franchise: String, searchQuery: String?, page: Int, limit: Int): Single<Map<String, String>> {
        val queryMap = ArrayMap<String, String>()

        if (!TextUtils.isEmpty(searchQuery)) {
            queryMap[SearchConstants.SEARCH] = searchQuery
        }

        if (franchise.isNotEmpty()) {
            queryMap[SearchConstants.FRANCHISE] = franchise
            queryMap[SearchConstants.PAGE] = page.toString()
            queryMap[SearchConstants.LIMIT] = limit.toString()

            queryMap[SearchConstants.CENSORED] = (!settingsSource.allowR18Content).toString()
        }
        return Single.just(queryMap)
    }

    private fun getDefaultRequest(page: Int, limit: Int): MutableMap<String, String> {
        val queryMap = ArrayMap<String, String>()
        queryMap[SearchConstants.PAGE] = page.toString()
        queryMap[SearchConstants.LIMIT] = limit.toString()
        queryMap[SearchConstants.ORDER] = SearchConstants.ORDER_BY.RANKED.toString()

        queryMap[SearchConstants.CENSORED] = (!settingsSource.allowR18Content).toString()
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