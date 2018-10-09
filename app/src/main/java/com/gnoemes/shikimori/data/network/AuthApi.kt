package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.entity.app.Constants
import com.gnoemes.shikimori.entity.app.TokenResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/oauth/token")
    fun getAccessToken(@Query("grant_type") grantType: String,
                       @Query("client_id") clientId: String = BuildConfig.ShikimoriClientId,
                       @Query("client_secret") clientSecret: String = BuildConfig.ShikimoriClientSecret,
                       @Query("code") code: String? = null,
                       @Query("redirect_uri") redirectUri: String? = Constants.REDIRECT_URI,
                       @Query("refresh_token") refreshToken: String? = null
    ): Single<TokenResponse>
}