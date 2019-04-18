package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.*
import io.reactivex.Single
import retrofit2.http.*

interface VideoApi {

    @GET("/api/anime/{id}/series")
    fun getEpisodes(@Path("id") id: Long): Single<List<EpisodeResponse>>

    @GET("/api/anime/alternative/{id}/series")
    fun getEpisodesAlternative(@Path("id") id: Long): Single<List<EpisodeResponse>>

    @GET("/api/anime/{animeId}/{episodeId}/translations")
    fun getTranslations(@Path("animeId") animeId: Long,
                        @Path("episodeId") episodeId: Long,
                        @Query("type") type: String
    ): Single<List<TranslationResponse>>

    @GET("/api/anime/alternative/{animeId}/{episodeId}/translations")
    fun getTranslationsAlternative(@Path("animeId") animeId: Long,
                                   @Path("episodeId") episodeId: Long,
                                   @Query("type") type: String
    ): Single<List<TranslationResponse>>

    @GET("/api/anime/{animeId}/{episodeId}/video/{videoId}")
    fun getVideo(@Path("animeId") animeId: Long,
                 @Path("episodeId") episodeId: Int,
                 @Path("videoId") videoId: String,
                 @Query("language") language: String,
                 @Query("kind") type: String,
                 @Query("author") author: String,
                 @Query("hosting") hosting: String
    ): Single<VideoResponse>

    @POST("/api/anime/player")
    fun getVideo(@Body request : VideoRequest) : Single<VideoResponse>

    @GET("https://api.vk.com/method/video.get?v=5.92&count=1&extended=0")
    fun getVkVideoFiles(@Query("access_token") token: String,
                        @Query("videos") videoId: String?
    ) : Single<VkResponse>

    @GET("/api/anime/alternative/translation/{id}")
    fun getVideoAlternative(@Path("id") translationId: Long): Single<VideoResponse>

    @GET("/api/anime/{animeId}/{episodeId}/topic")
    fun getTopic(@Path("animeId") animeId : Long,
                 @Path("episodeId") episodeId : Int
    ) : Single<EpisodeTopicIdResponse>
}