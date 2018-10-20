package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

sealed class DetailsAction {
    object EditRate : DetailsAction()
    data class ChangeRateStatus(val newStatus: RateStatus) : DetailsAction()
    data class GenreClicked(val genre: Genre) : DetailsAction()
    data class Video(val url: String) : DetailsAction()
    data class StudioClicked(val id: Long) : DetailsAction()
    object WatchOnline : DetailsAction()
    object Links : DetailsAction()
    object Discussion : DetailsAction()
    object OpenInBrowser : DetailsAction()
    object Chronology : DetailsAction()
    object ClearHistory : DetailsAction()
    object Screenshots : DetailsAction()
}