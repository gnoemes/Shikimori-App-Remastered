package com.gnoemes.shikimori.presentation.presenter.common.paginator

interface Paginator {
    fun restart()
    fun refresh()
    fun loadNewPage()
    fun release()
}