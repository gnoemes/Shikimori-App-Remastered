package com.gnoemes.shikimori.presentation.presenter.common.paginator

interface State<T> {
    fun restart() = Unit
    fun refresh() = Unit
    fun loadNewPage() = Unit
    fun release() = Unit
    fun newData(data: List<T>) = Unit
    fun error(error: Throwable) = Unit
}