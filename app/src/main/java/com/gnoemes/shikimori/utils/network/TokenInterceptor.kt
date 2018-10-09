package com.gnoemes.shikimori.utils.network

import com.gnoemes.shikimori.data.repository.app.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
        private val repository: TokenRepository
) : Interceptor {

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
        if (repository.isTokenExists()) {
            requestBuilder.addHeader(ACCESS_TOKEN_HEADER, String.format("Bearer %s", repository.getToken()?.authToken))
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}