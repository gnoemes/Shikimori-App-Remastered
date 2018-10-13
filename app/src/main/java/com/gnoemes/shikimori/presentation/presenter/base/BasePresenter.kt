package com.gnoemes.shikimori.presentation.presenter.base

import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import ru.terrakok.cicerone.Router

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    abstract val router: Router

    abstract fun onBackPressed()

    abstract fun initData()

    protected open fun onViewReattached() = Unit

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initData()
    }

    override fun attachView(view: View) {
        super.attachView(view)
        onViewReattached()
    }
}