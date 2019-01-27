package com.gnoemes.shikimori.presentation.view.player.embedded

import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView

interface EmbeddedPlayerView : BaseNetworkView {

    fun enableNextButton()
    fun disableNextButton()
    fun enablePrevButton()
    fun disablePrevButton()

    fun showMessage(s: String, exit : Boolean = false)

    fun selectTrack(currentTrack: Int)
    fun setEpisodeSubtitle(currentEpisode: Int)

    fun playVideo(it: Track, needReset: Boolean)

}