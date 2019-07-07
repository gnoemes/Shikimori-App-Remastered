package com.gnoemes.shikimori.entity.common.presentation

sealed class SortAction {
    data class ChangeSort(val sorts : List<Triple<RateSort, String, Boolean>>) : SortAction()
    data class ChangeOrder(val isDescending: Boolean) : SortAction()
}