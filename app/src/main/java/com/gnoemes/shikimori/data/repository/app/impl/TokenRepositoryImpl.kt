package com.gnoemes.shikimori.data.repository.app.impl

import com.gnoemes.shikimori.data.repository.app.TokenRepository
import com.gnoemes.shikimori.data.repository.app.TokenSource
import com.gnoemes.shikimori.entity.app.domain.Token
import io.reactivex.Completable
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
        private val source: TokenSource
) : TokenRepository {

    private var token: Token? = null

    override fun saveToken(token: Token?): Completable {
        this.token = token
        return source.saveToken(token)
    }

    override fun getToken(): Token? {
        if (token == null) {
            token = source.getToken()
            return token
        }
        return token
    }

    override fun isTokenExists(): Boolean = source.isTokenExists()
}