package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.shimori.ShimoriTranslationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ShimoriVideoApi {

    @GET("/api/anime/episodes")
    fun getEpisodes(
            @Query("id") malId: Long,
            @Query("name") name: String
    ) : Single<Int>

    @GET("/api/anime/query")
    fun getTranslations(
            @Query("id") malId: Long,
            @Query("name") name: String,
            @Query("episode") episode: Int,
            @Query("kind") translationType: String?
    ): Single<List<ShimoriTranslationResponse>>


}