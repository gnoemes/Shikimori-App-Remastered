package com.gnoemes.shikimori.domain.auth

import io.reactivex.Completable

interface AuthInteractor {

    fun signIn(authCode: String): Completable
}