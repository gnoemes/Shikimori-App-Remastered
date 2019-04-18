package com.gnoemes.shikimori.data.network

import io.reactivex.Single
import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DocumentVideoApi {

    @GET("/animes/a{animeId}/video_online/")
    fun getEpisodes(@Path("animeId") animeId: Long,
                    @Header("User-Agent") agent: String
    ): Single<Document>

    @GET("/animes/a{animeId}/video_online/{episode}")
    fun getTranslations(@Path("animeId") animeId: Long,
                        @Path("episode") episode: Int,
                        @Header("User-Agent") agent: String
    ): Single<Document>

    @GET("/animes/a{animeId}/video_online/{episode}/{videoId}")
    fun getVideo(@Path("animeId") animeId: Long,
                 @Path("episode") episode: Int,
                 @Path("videoId") videoId: Long,
                 @Header("User-Agent") agent: String
    ): Single<Document>
}