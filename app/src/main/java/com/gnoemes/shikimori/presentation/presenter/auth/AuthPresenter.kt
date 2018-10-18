package com.gnoemes.shikimori.presentation.presenter.auth

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.auth.AuthInteractor
import com.gnoemes.shikimori.entity.auth.AuthType
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.auth.AuthView
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
        private val interactor: AuthInteractor
) : BaseNetworkPresenter<AuthView>() {

    //Todo mb wizard?
    lateinit var authType: AuthType

    override fun initData() {
        when (authType) {
            AuthType.SIGN_IN -> viewState.onSignIn()
            AuthType.SIGN_UP -> viewState.onSignUp()
        }
    }

    fun onAuthCodeReceived(authCode: String?) {
        interactor.signIn(authCode ?: "")
                .subscribe(this::onSuccess, this::processErrors)
                .addToDisposables()
    }

    private fun onSuccess() {
        viewState.onBackPressed()
    }
}