package com.gnoemes.shikimori.presentation.presenter.series

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.download.DownloadInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.download.DownloadVideoData
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.*
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.ShareResourceProvider
import com.gnoemes.shikimori.presentation.presenter.series.translations.converter.TranslationsViewModelConverter
import com.gnoemes.shikimori.presentation.view.series.SeriesView
import com.gnoemes.shikimori.utils.Utils
import com.gnoemes.shikimori.utils.appendLoadingLogic
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class SeriesPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val downloadInteractor: DownloadInteractor,
        private val settingsSource: SettingsSource,
        private val converter: TranslationsViewModelConverter,
        private val commonResourceProvider: CommonResourceProvider,
        private val shareResourceProvider: ShareResourceProvider
) : BaseNetworkPresenter<SeriesView>() {

    lateinit var navigationData: SeriesNavigationData
    lateinit var type: TranslationType

    private var episode: Int? = null
    private var episodeId: Long? = null
    private var isAlternative: Boolean = false
    private var setting: TranslationSetting? = null
    private var query: String? = null
    private var rateId: Long = Constants.NO_ID

    private val items = mutableListOf<TranslationViewModel>()
    private lateinit var selectedVideo: TranslationVideo
    private var selectedDownloadUrl: String? = null
    private var selectedDownloadVideo: Video? = null
    private var selectedPlayer: PlayerType? = null

    private var isWatchSession = false

    override fun initData() {
        super.initData()
        type = settingsSource.translationType
        episode = if (navigationData.episodesAired < navigationData.episode ?: 0) navigationData.episodesAired else navigationData.episode
        episodeId = episode?.toLong()
        rateId = navigationData.rateId ?: rateId

        viewState.setBackground(navigationData.image)
        viewState.setTitle(navigationData.name)

        if (episode != null) {
            viewState.setEpisodeName(episode!!)
            viewState.showNextEpisode(episode != navigationData.episodesAired)
        } else viewState.showFab(false)

        if (navigationData.episodesAired == 1) {
            viewState.hideEpisodeName()
            viewState.showNextEpisode(false)
        }

        loadWithEpisode()
        analyzeType(type)
    }

    override fun onViewReattached() {
        super.onViewReattached()

        if (isWatchSession) {
            loadWithEpisode()
        }
    }

    private fun loadWithEpisode() {
        if (episodeId != null) loadData(episodeId!!)
        else loadEpisodes()
    }

    private fun loadData(episodeId: Long) =
            loadSettingsIfNeed()
                    .flatMap { loadTranslations(it, episodeId) }
                    .appendLoadingLogic(viewState)
                    .doOnSubscribe { viewState.showEmptyAuthorsView(false) }
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun loadEpisodes() = interactor.getEpisodes(navigationData.animeId, navigationData.nameEng, isAlternative)
            .map { it.take(navigationData.episodesAired) }
            .doOnSubscribe { viewState.showEpisodeLoading(true) }
            .doOnSuccess { viewState.showEpisodeLoading(false) }
            .subscribe(this::openPriorityEpisode, this::processErrors)
            .addToDisposables()

    private fun loadTranslations(type: TranslationType, episodeId: Long) = interactor
            .getTranslations(type, navigationData.animeId, episodeId, navigationData.nameEng, isAlternative, true)
            .doOnSubscribe { viewState.setTranslationType(type) }
            .map { converter.convertTranslations(it, setting) }

    private fun loadSettingsIfNeed(): Single<TranslationType> = (if (setting == null && settingsSource.useLocalTranslationSettings) loadSettings() else Single.just(type))

    private fun loadSettings() =
            interactor.getTranslationSettings(navigationData.animeId)
                    .doOnSuccess { setting = it }
                    .map { type = it.lastType ?: settingsSource.translationType;type }

    fun onRefresh() = loadWithEpisode()

    private fun openPriorityEpisode(items: List<Episode>) {
        if (episode == null) episode = if (!isWatchSession) items.firstOrNull { !it.isWatched }?.index ?: items.lastOrNull { it.isWatched }?.index else items.lastOrNull { it.isWatched }?.index ?: items.firstOrNull()?.index
        episodeId = items.firstOrNull { it.index == episode }?.id
        isWatchSession = false
        if (episode != null && episodeId != null) {
            viewState.showNextEpisode(episode != navigationData.episodesAired)
            viewState.setEpisodeName(episode!!)
            loadData(episodeId!!)
            viewState.showFab(true)
        } else if (navigationData.episodesAired > 0) {
            if (episode == null) episode = 1
            viewState.setEpisodeName(episode!!)
            viewState.onHideLoading()
            viewState.hideEmptyView()
            viewState.showNextEpisode(episode != navigationData.episodesAired)
            viewState.showEmptyAuthorsView(true, isAlternative)
            viewState.showContent(false)
            viewState.showFab(true)
        } else {
            viewState.onHideLoading()
            viewState.showEmptyView()
            viewState.hideEpisodeName()
            viewState.showNextEpisode(false)
            viewState.showFab(false)
        }
    }

    private fun setData(data: List<TranslationViewModel>) {
        val items = data.toMutableList()
        if (items.find { it.isSameAuthor } != null) {
            val priorityItem = items[items.indexOfFirst { it.isSameAuthor }]
            items.remove(priorityItem)
            items.add(0, priorityItem)
        }
        this@SeriesPresenter.items.clearAndAddAll(items)
        showData(items)
    }

    private fun showData(it: List<TranslationViewModel>) {
        if (!query.isNullOrBlank() && it.isEmpty()) viewState.showEmptySearchView()
        else if (it.isEmpty()) {
            viewState.showEmptyAuthorsView(true, isAlternative)
            viewState.showContent(false)
        } else viewState.showData(it)
    }

    fun onNextEpisode() = interactor.getEpisodes(navigationData.animeId, navigationData.nameEng, isAlternative)
            .map { it.take(navigationData.episodesAired) }
            .doOnSubscribe { viewState.showEpisodeLoading(true) }
            .doOnSuccess { viewState.showEpisodeLoading(false) }
            .subscribe(this::loadNextEpisode, this::processErrors)
            .addToDisposables()


    private fun loadNextEpisode(items: List<Episode>) {
        episode = items.firstOrNull { it.index == episode?.plus(1) }?.index ?: episode
        episodeId = items.firstOrNull { it.index == episode }?.id
        if (episode != null && episodeId != null) {
            viewState.showNextEpisode(episode != navigationData.episodesAired)
            viewState.setEpisodeName(episode!!)
            loadData(episodeId!!)
            viewState.showFab(true)
        } else if (navigationData.episodesAired > 0) {
            if (episode == null) episode = 0
            episode = episode?.plus(1)
            viewState.setEpisodeName(episode!!)
            viewState.showNextEpisode(episode != navigationData.episodesAired)
            viewState.onHideLoading()
            viewState.hideEmptyView()
            viewState.showEmptyAuthorsView(true, isAlternative)
            viewState.showContent(false)
            viewState.showFab(true)
        } else {
            viewState.onHideLoading()
            viewState.showEmptyView()
            viewState.hideEpisodeName()
            viewState.showNextEpisode(false)
            viewState.showFab(false)
        }
    }

    fun onSearchClicked() {
        viewState.showSearchView()
        logEvent(AnalyticEvent.ANIME_TRANSLATIONS_SEARCH_OPENED)
    }

    fun onSearchClose() = viewState.onSearchClosed()

    fun onMenuClicked(category: TranslationMenu) = when (category) {
        is TranslationMenu.Download -> showDownloadDialog(category.videos)
        is TranslationMenu.Author -> showAuthorDialog(category.author)
    }

    fun onEpisodeSelected(episodeId: Long, episode: Int, alternative: Boolean) {
        this.episodeId = episodeId
        this.episode = episode

        viewState.setEpisodeName(episode)
        viewState.showNextEpisode(episode != navigationData.episodesAired)

        if (isAlternative != alternative) {
            viewState.changeSource(alternative)
            isAlternative = alternative
        }
        loadWithEpisode()
    }

    fun onShare(url: String) {
        val videoUrl = if (url.contains("m3u8")) {
            url.replaceAfterLast("mp4", "")
        } else url

        val text = shareResourceProvider.getEpisodeShareFormattedMessage(navigationData.name, episode!!, videoUrl)
        router.navigateTo(Screens.SHARE, text)
    }

    private fun showAuthorDialog(author: String) {
        logEvent(AnalyticEvent.ANIME_TRANSLATIONS_AUTHORS)
        viewState.showAuthorDialog(author)
    }

    private fun showDownloadDialog(videos: List<TranslationVideo>) {
        val filteredItems = videos.filter { Utils.isHostingSupports(it.videoHosting) }

        Observable.fromIterable(filteredItems)
                .flatMapSingle { interactor.getVideo(it, it.videoHosting is VideoHosting.SMOTRET_ANIME) }
                .flatMap { video ->
                    Observable.just(video)
                            .flatMapIterable { it.tracks }
                            .map { converter.convertTrack(video, it) }
                }
                .toList()
                .appendLoadingLogic(viewState)
                .subscribe({ viewState.showDownloadDialog(filteredItems.first().author, it) }, this::processErrors)
                .addToDisposables()
    }

    fun showEpisodes() {
        val data = EpisodesNavigationData(navigationData.animeId, navigationData.nameEng, episode!!, rateId, isAlternative)
        viewState.showEpisodesDialog(data)
    }

    fun onTypeChanged(newType: TranslationType) {
        if (type == newType) return
        this.type = newType
        loadWithEpisode()
        analyzeType(newType)
    }

    fun onSourceChanged(alternative: Boolean) {
        if (isAlternative == alternative) return

        viewState.showData(emptyList())
        viewState.changeSource(alternative)
        isAlternative = alternative
        episodeId = null
        loadWithEpisode()
    }

    fun onRateCreated(rateId: Long) {
        this.rateId = rateId
    }

    fun onDiscussionClicked() {
        if (episode != null) {
            logEvent(AnalyticEvent.ANIME_TRANSLATIONS_DISCUSSION)
            interactor.getTopic(navigationData.animeId, episode!!)
                    .subscribe(this::onTopicClicked, this::onDiscussionNotExist)
                    .addToDisposables()
        }
    }

    fun onQueryChanged(newText: String?) {
        query = newText

        if (newText.isNullOrBlank()) showData(items)
        else {
            val searchItems = items.filter { it.authors.contains(newText, true) }
            showData(searchItems)
        }

        viewState.scrollToPosition(0)
    }

    fun onPlayerSelected(playerType: PlayerType) {
        openVideo(selectedVideo, playerType)
    }

    fun onHostingClicked(video: TranslationVideo) {
        this.selectedVideo = video
        if (!Utils.isHostingSupports(video.videoHosting)) openVideo(video, PlayerType.WEB)
        else if (!settingsSource.isAskForPlayer) openVideo(video, settingsSource.playerType)
        else viewState.showPlayerDialog()
    }

    //Only embedded player can process object payload
    //Others o uses urls
    private fun openVideo(payload: TranslationVideo, playerType: PlayerType) {
        if (playerType == PlayerType.EMBEDDED) openPlayer(playerType, EmbeddedPlayerNavigationData(navigationData.name, navigationData.rateId, items.firstOrNull()!!.episodesSize, payload, navigationData.nameEng, isAlternative))
        else if (playerType == PlayerType.WEB && payload.webPlayerUrl != null) openPlayer(playerType, payload.webPlayerUrl)
        else getVideoAndExecute(payload) { selectedPlayer = playerType; showQualityChooser(it.tracks) }
    }

    private fun showQualityChooser(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            viewState.showTracksNotFoundError()
            return
        }
        if (tracks.size == 1 || settingsSource.isExternalBestQuality) openPlayer(selectedPlayer!!, tracks.firstOrNull()?.url)
        else viewState.showQualityChooser(tracks.map { Pair(it.quality, it.url) })
    }

    fun onQualityChoosed(url: String?) {
        openPlayer(selectedPlayer!!, url)
    }

    override fun openPlayer(playerType: PlayerType, payload: Any?) {
        super.openPlayer(playerType, payload)

        isWatchSession = true
        saveSettingsAndIncrementOptional(playerType != PlayerType.EMBEDDED, selectedVideo)
    }

    private fun saveSettingsAndIncrementOptional(increment: Boolean, payload: TranslationVideo) {
        (if (settingsSource.isAutoIncrement && increment) interactor.sendEpisodeChanges(EpisodeChanges.Changes(rateId, payload.animeId, episode!!, true))
        else Completable.complete())
                .andThen(interactor.saveTranslationSettings(TranslationSetting(payload.animeId, payload.author, payload.type)))
                .subscribe({}, this::processErrors)
                .addToDisposables()
    }

    private fun getVideoAndExecute(payload: TranslationVideo, onSubscribe: (Video) -> Unit) {
        interactor.getVideo(payload, isAlternative)
                .appendLoadingLogic(viewState)
                .subscribe(onSubscribe::invoke, this::processErrors)
                .addToDisposables()
    }

    fun onTrackForDownloadSelected(url: String, video: Video) {
        selectedDownloadUrl = if (video.hosting is VideoHosting.KODIK) {
            url.replaceAfterLast("mp4", "")
        } else url
        selectedDownloadVideo = video
        viewState.checkPermissions()
    }

    fun onStoragePermissionsAccepted() {
        val downloadPath = settingsSource.downloadFolder

        if (downloadPath.isNotEmpty()) downloadVideo(selectedDownloadUrl, selectedDownloadVideo)
        else viewState.showFolderChooserDialog()
    }

    private fun downloadVideo(url: String?, video: Video?) {
        logEvent(AnalyticEvent.ANIME_TRANSLATIONS_DOWNLOAD)
        val data = DownloadVideoData(navigationData.animeId, navigationData.name, episode!!, url, Utils.getRequestHeadersForHosting(video))
        downloadInteractor.downloadVideo(data)
                .subscribe({}, this::processDownloadErrors)
                .addToDisposables()
    }

    private fun analyzeType(type: TranslationType) = when (type) {
        TranslationType.VOICE_RU -> logEvent(AnalyticEvent.ANIME_TRANSLATIONS_TYPE_VOICE_RU)
        TranslationType.SUB_RU -> logEvent(AnalyticEvent.ANIME_TRANSLATIONS_TYPE_SUB_RU)
        TranslationType.RAW -> logEvent(AnalyticEvent.ANIME_TRANSLATIONS_TYPE_ORIGINAL)
        else -> Unit
    }

    private fun onDiscussionNotExist(throwable: Throwable?) {
        router.showSystemMessage(commonResourceProvider.topicNotFound)
        viewState.showFab(false)
    }

    private fun processDownloadErrors(throwable: Throwable) {
    }
}