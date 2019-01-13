package com.gnoemes.shikimori.presentation.presenter.common.paginator

interface ViewController<T> {

    fun showEmptyError(show: Boolean, throwable: Throwable? = null)

    fun showEmptyView(show: Boolean)

    fun showData(show: Boolean, data: List<T> = emptyList())

    fun showRefreshProgress(show: Boolean)

    fun showPageProgress(show: Boolean)

    fun showEmptyProgress(show: Boolean) = Unit

    fun showError(throwable: Throwable)

    fun onAllData() = Unit
}