package com.gnoemes.shikimori.entity.common.presentation

sealed class SortAction {
    object ChangeSort : SortAction()
    data class ChangeOrder(val isDescending: Boolean) : SortAction()
}