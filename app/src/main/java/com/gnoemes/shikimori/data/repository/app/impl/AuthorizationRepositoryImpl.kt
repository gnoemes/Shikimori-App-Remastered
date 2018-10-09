package com.gnoemes.shikimori.data.repository.app.impl

import com.gnoemes.shikimori.data.network.AuthApi
import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository
import com.gnoemes.shikimori.entity.app.domain.Token
import io.reactivex.Single
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
        private val api: AuthApi
) : AuthorizationRepository {

    companion object {
        private const val AUTH_CODE = "authorization_code"
        private const val REFRESH_TOKEN = "refresh_token"
    }

    override fun signIn(authCode: String): Single<Token> =
            api.getAccessToken(AUTH_CODE, code = authCode)
                    .map { Token(it.accessToken, it.refreshToken) }

    override fun refreshToken(refreshToken: String): Single<Token> =
            api.getAccessToken(REFRESH_TOKEN, refreshToken = refreshToken)
                    .map { Token(it.accessToken, it.refreshToken) }
}