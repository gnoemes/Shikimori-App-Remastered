package com.gnoemes.shikimori.presentation.view.auth

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : BaseFragmentView {
    /**
     * Auth
     */
    fun onSignIn()

    /**
     * Reg.
     */
    fun onSignUp()

    fun onAnime365()
}