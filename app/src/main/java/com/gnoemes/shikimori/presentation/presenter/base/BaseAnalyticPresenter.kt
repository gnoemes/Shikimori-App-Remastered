package com.gnoemes.shikimori.presentation.presenter.base

import com.gnoemes.shikimori.domain.app.AnalyticInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import javax.inject.Inject

abstract class BaseAnalyticPresenter<View : BaseView> : BasePresenter<View>() {

    @Inject
    protected lateinit var analyticInteractor: AnalyticInteractor

    protected open fun logEvent(event : AnalyticEvent) = analyticInteractor.logEvent(event)
}