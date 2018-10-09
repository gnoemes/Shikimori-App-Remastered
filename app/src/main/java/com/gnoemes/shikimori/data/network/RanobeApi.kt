package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.common.data.FranchiseResponse
import com.gnoemes.shikimori.entity.common.data.LinkResponse
import com.gnoemes.shikimori.entity.common.data.RelatedResponse
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RanobeApi {

    @GET("/api/ranobe")
    fun getList(@QueryMap(encoded = true) filter: Map<String, String>): Single<List<MangaResponse>>

    @GET("/api/ranobe/{id}")
    fun getDetails(@Path("id") id: Long): Single<MangaDetailsResponse>

    @GET("/api/ranobe/{id}/roles")
    fun getRoles(@Path("id") id: Long): Single<List<RolesResponse>>

    @GET("/api/ranobe/{id}/similar")
    fun getSimilar(@Path("id") id: Long): Single<List<MangaResponse>>

    @GET("/api/ranobe/{id}/related")
    fun getRelated(@Path("id") id: Long): Single<List<RelatedResponse>>

    @GET("/api/ranobe/{id}/franchise")
    fun getFranchise(@Path("id") id: Long): Single<FranchiseResponse>

    @GET("/api/ranobe/{id}/external_links")
    fun getLinks(@Path("id") id: Long): Single<List<LinkResponse>>
}