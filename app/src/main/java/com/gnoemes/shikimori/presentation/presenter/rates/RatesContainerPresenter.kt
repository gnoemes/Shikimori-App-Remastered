package com.gnoemes.shikimori.presentation.presenter.rates

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter
import com.gnoemes.shikimori.presentation.view.rates.RatesContainerView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class RatesContainerPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: RateCountConverter
) : BaseNetworkPresenter<RatesContainerView>() {

    var type: Type = Type.ANIME
    var userId: Long = Constants.NO_ID
    private val isAnime: Boolean
        get() = type == Type.ANIME

    override fun initData() {
        if (userId != Constants.NO_ID) loadRateCategories()
        else loadMyUser()
    }

    private fun loadMyUser() =
            interactor.getMyUserBrief()
                    .doOnSuccess { userId = it.id }
                    .doOnSuccess { loadRateCategories() }
                    .subscribe({ }, this::processUserErrors)

    private fun loadRateCategories() =
            interactor.getDetails(userId)
                    .appendLoadingLogic(viewState)
                    .map {
                        if (isAnime) converter.countAnimeRates(it)
                        else converter.countMangaRates(it)
                    }
                    .subscribe({ viewState.setData(type, it) }, this::processErrors)
                    .addToDisposables()

    override fun processErrors(throwable: Throwable) {
        super.processErrors(throwable)
    }

    private fun processUserErrors(throwable: Throwable) {
        Log.i("DEVE", "user err $throwable")
    }

    fun onChangeType(type: Type) {
        this.type = type
        loadRateCategories()
    }
}