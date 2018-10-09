package com.gnoemes.shikimori.utils.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    companion object {
        const val USER_AGENT_HEADER = "User-agent"
        const val USER_AGENT_CLIENT = "Shikimori App"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
        requestBuilder.addHeader(USER_AGENT_HEADER, USER_AGENT_CLIENT)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}