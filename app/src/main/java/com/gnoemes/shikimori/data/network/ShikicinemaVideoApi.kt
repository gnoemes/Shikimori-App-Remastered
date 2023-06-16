package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.shikicinema.ShikicinemaEpisodesResponse
import com.gnoemes.shikimori.entity.series.data.shikicinema.ShikicinemaTranslationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShikicinemaVideoApi {

    @GET("/api/shikivideos/{animeId}/length")
    fun getEpisodes(
            @Path("animeId") animeId: Long
    ): Single<ShikicinemaEpisodesResponse>

    @GET("/api/shikivideos/{animeId}")
    fun getTranslations(
            @Path("animeId") animeId: Long,
            @Query("limit") limit: String,
            @Query("episode") episode: Long,
            @Query("kind") translationType: String?
    ): Single<List<ShikicinemaTranslationResponse>>
}
