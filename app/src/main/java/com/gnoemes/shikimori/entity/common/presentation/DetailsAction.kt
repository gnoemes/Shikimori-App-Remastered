package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

sealed class DetailsAction {
    data class ChangeRateStatus(val newStatus: RateStatus, val id: Long = Constants.NO_ID) : DetailsAction()
    data class GenreClicked(val genre: Genre) : DetailsAction()
    data class Video(val url: String) : DetailsAction()
    data class StudioClicked(val id: Long) : DetailsAction()
    data class WatchOnline(val id: Long? = null) : DetailsAction()
    data class Screenshots(val pos: Int) : DetailsAction()
    data class Link(val url: String, val share: Boolean) : DetailsAction()
    object EditRate : DetailsAction()
    object RateStatusDialog : DetailsAction()
    object Links : DetailsAction()
    object Similar : DetailsAction()
    object Statistic : DetailsAction()
    object Discussion : DetailsAction()
    object OpenInBrowser : DetailsAction()
    object Chronology : DetailsAction()
    object ClearHistory : DetailsAction()
    object AddVideo : DetailsAction()
    object Share : DetailsAction()

}