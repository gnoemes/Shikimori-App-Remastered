package com.gnoemes.shikimori.presentation.presenter.player

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.EmbeddedPlayerNavigationData
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.player.embedded.EmbeddedPlayerView
import com.gnoemes.shikimori.presentation.view.player.embedded.provider.EmbeddedPlayerResourceProvider
import com.gnoemes.shikimori.utils.Utils
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class EmbeddedPlayerPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val settingsSource: SettingsSource,
        private val resourceProvider: EmbeddedPlayerResourceProvider
) : BaseNetworkPresenter<EmbeddedPlayerView>() {

    lateinit var navigationData: EmbeddedPlayerNavigationData

    private var currentEpisode: Int = -1
    private var currentTrack = 0
    private lateinit var payload: TranslationVideo

    private val videos = hashSetOf<Video>()

    override fun initData() {
        super.initData()
        currentEpisode = navigationData.payload.episodeIndex
        payload = navigationData.payload

        viewState.setTitle(navigationData.animeName)

        loadVideo(payload)
        updateControls()
    }

    private fun loadVideo(payload: TranslationVideo) {
        interactor.getVideo(payload, false)
                .appendLoadingLogic(viewState)
                .subscribe({ updateVideo(it) }, this::processErrors)
                .addToDisposables()
    }

    private fun updateVideo(video: Video, needReset: Boolean = true) {
        videos.add(video)

        if (!Utils.isHostingSupports(video.hosting)) viewState.showMessage(resourceProvider.hostingErrorMessage)
        else if (video.tracks.isNotEmpty()) setTrack(video, needReset)
        else viewState.showMessage(resourceProvider.playerErrorMessage, true)
    }

    private fun setTrack(video: Video, needReset: Boolean) {
        val track = video.tracks.getOrNull(currentTrack)
        track?.let {
            viewState.apply {
                setEpisodeSubtitle(currentEpisode)
                playVideo(it, needReset, Utils.getRequestHeadersForHosting(video))
                val resolutions = video.tracks.asSequence().filter { it.quality != "unknown" }.map { it.quality }.toList()
                setResolutions(resolutions)
                selectTrack(currentTrack)
            }
            currentTrack = 0
            setEpisodeWatched()
        } ?: viewState.showMessage(resourceProvider.playerErrorMessage)
    }

    private fun updateControls() {
        viewState.enableNextButton(currentEpisode < navigationData.episodesSize)
        viewState.enablePrevButton(currentEpisode > 1)
    }

    private fun loadOrUpdateVideo(payload: TranslationVideo) {
        val video = videos.find { it.episodeId.toInt() == currentEpisode }
        video?.let { updateVideo(video) } ?: loadVideo(payload)
    }

    private fun setEpisodeWatched() {
        if (!settingsSource.isAutoIncrement) return

        interactor
                .sendEpisodeChanges(EpisodeChanges(animeId, currentEpisode, true))
                .subscribe({}, this::processErrors)
                .addToDisposables()
    }

    fun loadNextEpisode() {
        currentEpisode += 1
        payload = payload.copy(videoId = Constants.NO_ID, episodeIndex = currentEpisode)
        if (currentEpisode <= navigationData.episodesSize) loadOrUpdateVideo(payload)
        updateControls()
    }

    fun loadPrevEpisode() {
        currentEpisode -= 1
        payload = payload.copy(videoId = Constants.NO_ID, episodeIndex = currentEpisode)
        if (currentEpisode > 0) loadOrUpdateVideo(payload)
        updateControls()
    }

    fun onResolutionChanged(newResolution: String) {
        val video = videos.find { it.episodeId.toInt() == currentEpisode }
        val track = video?.tracks?.find { it.quality == newResolution }
        track?.let {
            currentTrack = video.tracks.indexOf(it)
            updateVideo(video, false)
        }
    }

    private val animeId: Long
        get() = navigationData.payload.animeId

}