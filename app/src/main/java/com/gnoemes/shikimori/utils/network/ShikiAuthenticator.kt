package com.gnoemes.shikimori.utils.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class ShikiAuthenticator @Inject constructor(
        private val holder: AuthHolder
) : Authenticator {

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val storedToken = "Bearer ${holder.getToken()?.authToken}"
        val requestToken = response.request().header(ACCESS_TOKEN_HEADER)

        val builder = response.request().newBuilder()

        if (storedToken == requestToken) {
            holder.refresh()
        }

        return builder.header(ACCESS_TOKEN_HEADER, "Bearer ${holder.getToken()?.authToken}").build()
    }
}