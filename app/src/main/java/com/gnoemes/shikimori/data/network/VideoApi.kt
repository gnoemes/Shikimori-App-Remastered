package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.*
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
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

    @Headers("Accept: text/html")
    @GET
    fun getPlayerHtml(@Url playerUrl: String) : Single<ResponseBody>

    @Headers("Accept: text/plain")
    @GET
    fun getTextResponse(@Url playerUrl: String) : Single<ResponseBody>

    @GET
    fun getMailRuVideoMeta(@Url videoMetaUrl: String) : Single<Response<MailRuVideosResponse>>

    @GET
    fun getNuumStreamsMetadata(@Url metadataUrl: String) : Single<Response<NuumStreamsMetadataResponse>>

    @GET("/api/anime/alternative/translation/{id}")
    fun getVideoAlternative(
            @Path("id") translationId: Long,
            @Query("accessToken") accessToken : String? = null
    ): Single<VideoResponse>

    @GET("/api/anime/{animeId}/{episodeId}/topic")
    fun getTopic(@Path("animeId") animeId : Long,
                 @Path("episodeId") episodeId : Int
    ) : Single<EpisodeTopicIdResponse>
}