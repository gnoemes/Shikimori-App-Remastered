package com.gnoemes.shikimori.utils.network

import com.gnoemes.shikimori.data.local.preference.UserSource
import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository
import com.gnoemes.shikimori.data.repository.app.TokenRepository
import com.gnoemes.shikimori.entity.app.domain.Token
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class AuthHolder @Inject constructor(
        private val tokenRepository: TokenRepository,
        private val authRepository: AuthorizationRepository,
        private val userRepository: UserSource
) {

    private val disposables = CompositeDisposable()

    fun getToken(): Token? = tokenRepository.getToken()

    private fun updateToken(): Completable {
        val token = getToken()

        if (token == null || token.refreshToken.isEmpty()) {
            return Completable.fromAction { userRepository.clearUser() }
        }

        return Single.fromCallable { token }
                .flatMap { authRepository.refreshToken(it.refreshToken) }
                .flatMapCompletable { tokenRepository.saveToken(it) }
    }

    fun refresh() {
        disposables.add(
                updateToken()
                        .subscribe({ /* onComplete — no-op */ }, { error ->
                            if (error is HttpException) {
                                userRepository.clearUser()
                                tokenRepository.saveToken(null).blockingGet()
                            }
                        })
        )
    }

    fun onCleared() {
        disposables.clear()
    }

}