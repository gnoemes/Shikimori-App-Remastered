package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.plashiki.PlashikiTranslationsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PlashikiVideoApi {

    @GET("/api/v2/anime/{animeId}?raw")
    fun getTranslations(@Path("animeId") animeId: Long): Single<PlashikiTranslationsResponse>



}