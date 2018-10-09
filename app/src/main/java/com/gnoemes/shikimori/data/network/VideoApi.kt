package com.gnoemes.shikimori.data.network

import io.reactivex.Single
import okhttp3.ResponseBody
import org.jsoup.nodes.Document
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface VideoApi {

    /**
     * Get html page of specific video
     */
    @GET("/animes/a{animeId}/video_online/{episode}/{videoId}")
    fun getAnimeVideoInfo(@Path("animeId") animeId: Long, @Path("episode") episode: Int, @Path("videoId") videoId: Long): Single<Document>

    /**
     * Get html page of anime (information about episodes hostings etc)
     */
    @GET("/animes/a{animeId}/video_online/")
    fun getAnimeVideoInfo(@Path("animeId") animeId: Long): Single<Document>

    /**
     * Get html page of anime with default video
     */
    @GET("/animes/a{animeId}/video_online/{episode}")
    fun getAnimeVideoInfo(@Path("animeId") animeId: Long, @Path("episode") episode: Int): Single<Document>

    /**
     * Get html source from hosting
     */
    @GET
    fun getVideoSource(@Url url: String): Single<Document>

    /**
     * Handle raw response with headers and redirect urls
     */
    @GET
    fun getRawVideoResponse(@Url url: String): Single<Response<ResponseBody>>
}