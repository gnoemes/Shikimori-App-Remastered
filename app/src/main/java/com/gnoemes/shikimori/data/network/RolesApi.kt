package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.roles.data.CharacterDetailsResponse
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.data.PersonDetailsResponse
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RolesApi {

    @GET("/api/characters/{id}")
    fun getCharacterDetails(@Path("id") characterId: Long): Single<CharacterDetailsResponse>

    @GET("/api/characters/search")
    fun getCharacterList(@QueryMap(encoded = true) filter: Map<String, String>): Single<List<CharacterResponse>>

    @GET("/api/people/{id}")
    fun getPersonDetails(@Path("id") peopleId: Long): Single<PersonDetailsResponse>

    @GET("/api/people/search")
    fun getPersonList(@QueryMap(encoded = true) filter: Map<String, String>): Single<List<PersonResponse>>

}