package com.gnoemes.shikimori.presentation.presenter.player

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.EmbeddedPlayerNavigationData
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.player.embedded.EmbeddedPlayerView
import com.gnoemes.shikimori.utils.Utils
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class EmbeddedPlayerPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor
) : BaseNetworkPresenter<EmbeddedPlayerView>() {

    lateinit var navigationData: EmbeddedPlayerNavigationData

    private var currentEpisode: Int = -1
    private var currentTrack = 0
    private var rateId: Long = Constants.NO_ID
    private lateinit var payload: TranslationVideo

    private val videos = hashSetOf<Video>()

    override fun initData() {
        super.initData()
        navigationData.rateId?.let { rateId = it }
        currentEpisode = navigationData.payload.episodeIndex
        payload = navigationData.payload

        viewState.setTitle(navigationData.animeName)

        loadVideo(payload)
        updateControls()
    }

    private fun loadVideo(payload: TranslationVideo) {
        interactor.getVideo(payload)
                .appendLoadingLogic(viewState)
                .subscribe({ updateVideo(it) }, this::processErrors)
                .addToDisposables()
    }

    private fun updateVideo(video: Video, needReset: Boolean = true) {
        videos.add(video)

        //TODO hosting error message
        if (!Utils.isHostingSupports(video.hosting)) viewState.showMessage("ss")
        else if (video.tracks.isNotEmpty()) setTrack(video.tracks, needReset)
        //TODO player error message
        else viewState.showMessage("ss", true)
    }

    private fun setTrack(tracks: List<Track>, needReset: Boolean) {
        val track = tracks.getOrNull(currentTrack)
        track?.let {
            viewState.apply {
                selectTrack(currentTrack)
                setEpisodeSubtitle(currentEpisode)
                playVideo(it, needReset)
            }
            currentTrack = 0
            setEpisodeWatched()
            //TODO track error message
        } ?: viewState.showMessage("ss")
    }

    private fun updateControls() {
        if (currentEpisode < navigationData.episodesSize) viewState.enableNextButton()
        else viewState.disableNextButton()

        if (currentEpisode > 1) viewState.enablePrevButton()
        else viewState.disablePrevButton()
    }

    private fun loadOrUpdateVideo(payload: TranslationVideo) {
        val video = videos.find { it.episodeId.toInt() == currentEpisode }
        video?.let { updateVideo(video) } ?: loadVideo(payload)
    }

    private fun setEpisodeWatched() {
        Single.just(rateId)
                .flatMap { createRateIfNotExist(it) }
                .flatMapCompletable { interactor.setEpisodeStatus(animeId, currentEpisode, it, true) }
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

    //TODO do something with rate creation, mb relocate logic to interactor
    private fun createRateIfNotExist(rateId: Long): Single<Long> {
        return when (rateId) {
            Constants.NO_ID -> ratesInteractor.createRateWithResult(animeId, Type.ANIME, RateStatus.WATCHING).map { it.id!! }.doOnSuccess { this@EmbeddedPlayerPresenter.rateId = it }
            else -> Single.just(rateId)
        }
    }

    private val animeId: Long
        get() = navigationData.payload.animeId

}