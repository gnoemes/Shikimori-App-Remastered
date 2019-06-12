package com.gnoemes.shikimori.entity.rates.domain

import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel

sealed class RateListAction {
    data class Pin(val rate : RateViewModel) : RateListAction()
    data class ChangeOrder(val rate : RateViewModel, val newOrder : Int) : RateListAction()
}