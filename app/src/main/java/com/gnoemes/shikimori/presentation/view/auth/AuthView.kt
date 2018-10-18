package com.gnoemes.shikimori.presentation.view.auth

import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface AuthView : BaseFragmentView {
    /**
     * Auth
     */
    fun onSignIn()

    /**
     * Reg.
     */
    fun onSignUp()
}