package com.gnoemes.shikimori.data.repository.app

import com.gnoemes.shikimori.entity.app.domain.Token
import io.reactivex.Completable

interface TokenSource {

    fun saveToken(token: Token?): Completable

    fun getToken(): Token?

    fun isTokenExists(): Boolean
}