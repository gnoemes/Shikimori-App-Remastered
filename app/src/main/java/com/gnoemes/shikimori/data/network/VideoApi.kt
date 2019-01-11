package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoApi {

    @GET("/api/anime/:id/series")
    fun getEpisodes(@Path("id") id: Long): Single<List<EpisodeResponse>>

    @GET("/api/anime/:animeId/:episodeId/translations")
    fun getTranslations(@Path("animeId") animeId: Long,
                        @Path("episodeId") episodeId: Int,
                        @Query("type") type: TranslationType
    ): Single<List<TranslationResponse>>

    @GET("/api/anime/:animeId/:episodeId/video/:videoId")
    fun getVideo(@Path("animeId") animeId: Long,
                 @Path("episodeId") episodeId: Int,
                 @Path("videoId") videoId : String,
                 @Query("language") language: String,
                 @Query("kind") type : TranslationType,
                 @Query("author") author : String,
                 @Query("hosting") hosting : VideoHosting
    ): Single<List<VideoResponse>>
}