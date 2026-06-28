package com.gnoemes.shikimori.presentation.view.series

import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.app.AnalyticInteractor
import com.gnoemes.shikimori.domain.download.DownloadInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.series.SeriesSyncInteractor
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.entity.series.presentation.SeriesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.presentation.presenter.series.SeriesPresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.ShareResourceProvider
import com.gnoemes.shikimori.presentation.presenter.series.translations.converter.TranslationsViewModelConverter
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Test for SeriesPresenter focusing on the race condition in openExternalPlayer().
 *
 * Root cause: In openExternalPlayer(), setEpisodeWatched().subscribe() is added to
 * compositeDisposable via addToDisposables(). When the view immediately detaches after
 * navigating to external player, BaseNavigationPresenter.compositeDisposable.clear()
 * cancels the async Rx chain on Schedulers.io() before the Room DB insert or network
 * API call completes.
 *
 * This test:
 * 1. Sets up a SeriesPresenter with mocked dependencies
 * 2. Simulates calling openExternalPlayer() followed by view detachment
 * 3. Verifies that setEpisodeWatched is still executed despite detachment
 */
@DisplayName("SeriesPresenter")
class SeriesPresenterTest {

    // ── Dependencies ──────────────────────────────────────────────

    private val interactor: SeriesInteractor = mockk()
    private val downloadInteractor: DownloadInteractor = mockk()
    private val settingsSource: SettingsSource = mockk()
    private val converter: TranslationsViewModelConverter = mockk()
    private val commonResourceProvider: CommonResourceProvider = mockk()
    private val shareResourceProvider: ShareResourceProvider = mockk()
    private val seriesSyncInteractor: SeriesSyncInteractor = mockk()
    private val analyticInteractor: AnalyticInteractor = mockk(relaxed = true)
    private val router: Router = mockk(relaxed = true)

    // ── View (relaxed to handle default state calls from initData) ──

    private val view: SeriesView = mockk(relaxed = true)

    // ── Presenter under test ──────────────────────────────────────

    private lateinit var presenter: SeriesPresenter

    // ── Shared test data ──────────────────────────────────────────

    private val translationVideo = TranslationVideo(
        videoId = 1L,
        animeId = 1L,
        episodeIndex = 1,
        language = "ru",
        author = "Test Author",
        authorSimple = "Test",
        type = TranslationType.VOICE_RU,
        videoHosting = VideoHosting.SMOTRET_ANIME(),
        webPlayerUrl = null,
        adLink = null
    )

    @BeforeEach
    fun setup() {
        // ── Mock Android main thread scheduler for unit tests ──
        // AndroidSchedulers.mainThread() requires android.os.Looper, which is
        // unavailable in standard JUnit tests. Use trampoline to run synchronously.
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        // ── Settings defaults ──
        every { settingsSource.translationType } returns TranslationType.VOICE_RU
        every { settingsSource.useLocalTranslationSettings } returns false
        every { settingsSource.playerType } returns PlayerType.EXTERNAL
        every { settingsSource.isAskForPlayer } returns false
        every { settingsSource.isExternalBestQuality } returns false
        every { settingsSource.isAutoIncrement } returns false

        // ── Mock interactor for initData flow ──
        // initData() -> loadWithEpisode() -> loadData() -> getTranslations()
        every { interactor.getTranslations(any(), any(), any(), any(), any()) } returns Single.just(emptyList())
        every { converter.convertTranslations(any(), any()) } returns emptyList()

        // ── Create presenter ──
        presenter = SeriesPresenter(
            interactor = interactor,
            downloadInteractor = downloadInteractor,
            settingsSource = settingsSource,
            converter = converter,
            commonResourceProvider = commonResourceProvider,
            shareResourceProvider = shareResourceProvider
        )

        // ── Inject lateinit fields ──
        presenter.seriesSyncInteractor = seriesSyncInteractor
        presenter.analyticInteractor = analyticInteractor
        presenter.localRouter = router

        // ── Navigation data (required before attachView -> initData) ──
        presenter.navigationData = SeriesNavigationData(
            animeId = 1L,
            image = Image(null, null, null, null),
            name = "Test Anime",
            nameEng = "test_anime",
            rateId = 1L,
            episodesAired = 12,
            episode = 1
        )

        // ── Set selectedVideo via reflection (private field) ──
        // This is normally set by onHostingClicked() before openExternalPlayer()
        val selectedVideoField = SeriesPresenter::class.java.getDeclaredField("selectedVideo")
        selectedVideoField.isAccessible = true
        selectedVideoField.set(presenter, translationVideo)

        // ── Attach view (triggers onFirstViewAttach -> initData) ──
        // This sets episodeId, rateId and loads initial data
        presenter.attachView(view)
    }

    @AfterEach
    fun teardown() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun `setEpisodeWatched should complete even after view detachment during external player navigation`() {
        // ── Track whether the delayed operation actually completed ──
        val watchedCompleted = AtomicBoolean(false)

        // ── Mock setEpisodeWatched to return a delayed Completable ──
        // This simulates an async operation (network call / DB insert on Schedulers.io())
        every {
            seriesSyncInteractor.setEpisodeWatched(any(), any(), any(), any())
        } answers {
            Completable
                .timer(200, TimeUnit.MILLISECONDS)
                .doOnComplete { watchedCompleted.set(true) }
        }

        // ── Act: open external player ──
        // This starts the setEpisodeWatched async chain and adds the
        // subscription to compositeDisposable via .addToDisposables()
        presenter.openExternalPlayer("test_payload")

        // ── Act: immediately detach view (simulates navigation away) ──
        // BaseNavigationPresenter.detachView() calls compositeDisposable.clear()
        // Before fix: this disposes the setEpisodeWatched subscription,
        // cancelling the timer before it fires.
        presenter.detachView(view)

        // ── Wait for the async operation to complete (if not cancelled) ──
        Thread.sleep(500)

        // ── Assert ──
        // Before fix: watchedCompleted stays FALSE because compositeDisposable.clear()
        //   disposed the subscription, cancelling the timer chain.
        //   → This assertion FAILS (test fails as expected).
        //
        // After fix: watchedCompleted is TRUE because the subscription is not
        //   tied to compositeDisposable, so the operation survives view detachment.
        //   → This assertion PASSES (test passes after fix).
        assertTrue(watchedCompleted.get()) {
            "setEpisodeWatched should complete even after view detachment. " +
            "Before fix: compositeDisposable.clear() cancels the async chain " +
            "(Room DB insert / network API call never completes). " +
            "After fix: the operation should survive view detachment."
        }
    }
}
