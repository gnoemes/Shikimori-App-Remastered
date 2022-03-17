package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.data.shimori.ShimoriEpisodeResponse
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

    @GET("/api/anime/series")
    fun getSeries(
            @Query("id") malId: Long,
            @Query("name") name: String
    ) : Single<List<ShimoriEpisodeResponse>>

    @GET("/api/anime/query")
    fun getTranslations(
            @Query("id") malId: Long,
            @Query("name") name: String,
            @Query("episode") episode: Int,
            @Query("hostingId") hostingId : Int,
            @Query("kind") translationType: String?
    ): Single<List<ShimoriTranslationResponse>>


    @GET("/api/anime/video")
    fun getVideo(
            @Query("id") malId: Long,
            @Query("episode") episode: Int,
            @Query("kind") translationType: String?,
            @Query("author") author: String?,
            @Query("hosting") hosting : String,
            @Query("hostingId") hostingId : Int,
            @Query("videoId") videoId : Long?,
            @Query("url") url : String?,
            @Query("accessToken") accessToken : String?
    ) : Single<VideoResponse>

}