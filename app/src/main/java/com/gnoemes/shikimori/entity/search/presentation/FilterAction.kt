package com.gnoemes.shikimori.entity.search.presentation

sealed class FilterAction {
    object Clear : FilterAction()
    object Invert : FilterAction()
    object SelectAll : FilterAction()
    object ShowNested : FilterAction()
}