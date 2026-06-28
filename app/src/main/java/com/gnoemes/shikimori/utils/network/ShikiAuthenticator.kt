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
        private val refreshLock = Any()
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val storedToken = "Bearer ${holder.getToken()?.authToken}"
        val requestToken = response.request.header(ACCESS_TOKEN_HEADER)

        val builder = response.request.newBuilder()

        if (storedToken == requestToken) {
            synchronized(refreshLock) {
                // Re-check token after acquiring lock — if another thread already refreshed,
                // the stored token will have changed and we can skip.
                val currentToken = "Bearer ${holder.getToken()?.authToken}"
                if (currentToken == requestToken) {
                    holder.refresh()
                }
            }
        }

        return builder.header(ACCESS_TOKEN_HEADER, "Bearer ${holder.getToken()?.authToken}").build()
    }
}