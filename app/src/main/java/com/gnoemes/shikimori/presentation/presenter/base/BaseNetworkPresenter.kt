package com.gnoemes.shikimori.presentation.presenter.base

import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseNetworkPresenter<View : BaseNetworkView> : BaseNavigationPresenter<View>() {

    private var compositeDisposable = CompositeDisposable()

    override fun initData() {}

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    fun Disposable.addToDisposables() {
        compositeDisposable.add(this)
    }

    protected open fun processErrors(throwable: Throwable) {
//        val errorUtils = ErrorUtils()
//        errorUtils.processErrors(throwable, router, viewState)
    }
}