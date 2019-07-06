package com.gnoemes.shikimori.presentation.presenter.common.provider

interface ShareResourceProvider {
    fun getEpisodeShareFormattedMessage(title : String, episode : Int, url : String) : String
}