package com.gnoemes.shikimori.data.repository.app

import com.gnoemes.shikimori.entity.app.domain.Token
import io.reactivex.Single

interface AuthorizationRepository {

    fun signIn(authCode: String): Single<Token>

    fun refreshToken(refreshToken: String): Single<Token>

}