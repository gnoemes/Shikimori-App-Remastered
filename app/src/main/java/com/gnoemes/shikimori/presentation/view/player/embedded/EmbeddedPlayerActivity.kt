package com.gnoemes.shikimori.presentation.view.player.embedded

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.PlayerSettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.VideoFormat
import com.gnoemes.shikimori.presentation.presenter.player.EmbeddedPlayerPresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseActivity
import com.gnoemes.shikimori.presentation.view.common.widget.CustomSpinner
import com.gnoemes.shikimori.presentation.view.common.widget.ReSpinner
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.exoplayer.MediaSourceHelper
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.android.synthetic.main.activity_embedded_player.*
import kotlinx.android.synthetic.main.layout_player_bottom.*
import kotlinx.android.synthetic.main.layout_player_controls.*
import kotlinx.android.synthetic.main.layout_player_rewind_forward.*
import kotlinx.android.synthetic.main.layout_player_toolbar.*
import org.joda.time.Duration
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class EmbeddedPlayerActivity : BaseActivity<EmbeddedPlayerPresenter, EmbeddedPlayerView>(),
    EmbeddedPlayerView {

    @InjectPresenter
    lateinit var playerPresenter: EmbeddedPlayerPresenter

    @ProvidePresenter
    fun providePresenter(): EmbeddedPlayerPresenter =
        presenterProvider.get()
            .apply { navigationData = intent.getParcelableExtra(AppExtras.ARGUMENT_PLAYER_DATA) }

    @Inject
    lateinit var localNavigatorHolder: NavigatorHolder

    @Inject
    lateinit var settingsSource: PlayerSettingsSource

    companion object {
        const val CONTROLLER_HIDE_DELAY = 3500L
        const val UNLOCK_HIDE_DELAY = 2000L
        const val FORWARD_REWIND_HIDE_DELAY = 750L
    }

    override var applyTheme: Boolean
        get() = false
        set(value) {}

    private val controller by lazy { PlayerController(settingsSource) }

    private val smallOffsetText by lazy { "${settingsSource.forwardRewindOffset / 1000} ${getString(R.string.player_seconds_short)}" }
    private val bigOffsetText by lazy { "${settingsSource.forwardRewindOffsetBig / 1000} ${getString(R.string.player_seconds_short)}" }
    private val brightnessIcon by lazy { drawable(R.drawable.ic_brightness) }
    private val volumeIcon by lazy { drawable(R.drawable.ic_volume) }
    private val offsetColor by lazy { color(R.color.player_unlock_background) }

    private var currentVolume: Int = 0
    private var currentBrightness: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ShikimoriAppTheme_Player)
        theme.applyStyle(getCurrentAscentTheme, true)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        currentVolume = audioManager().getStreamVolume(AudioManager.STREAM_MUSIC)
        currentBrightness = (window.attributes.screenBrightness / 2.55f).toInt()

        toolbar.addBackButton(icon = R.drawable.ic_arrow_back_player) { onBackPressed() }

        if (settingsSource.isOpenLandscape) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        forwardView.text = smallOffsetText
        forwardView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_forward, 0, 0)
        rewindView.text = smallOffsetText
        rewindView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_rewind, 0, 0)

        exo_progress.setBufferedColor(ColorUtils.setAlphaComponent(colorAttr(R.attr.colorSecondaryTransparent), 153))
        resolutionSpinnerView.background.tint(color(R.color.player_controls))

        speedSpinnerView.apply {
            background.tint(color(R.color.player_controls))
            adapter = ArrayAdapter(
                this@EmbeddedPlayerActivity,
                R.layout.item_spinner_player,
                resources.getStringArray(R.array.player_speed_rates)
            )
            setOnItemClickListener { _, _, position, _ -> controller.changePlaySpeed(position) }
            setSelection(2, false)
        }

        adView.apply {
            settings.apply {
                setAppCacheEnabled(true)
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                javaScriptCanOpenWindowsAutomatically = true
                javaScriptEnabled = true
                domStorageEnabled = true
                allowContentAccess = true
                allowUniversalAccessFromFileURLs = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mediaPlaybackRequiresUserGesture = false
                }
                userAgentString =
                    "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36"
            }

            addJavascriptInterface(AdInterface(), "AdHandler")

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view?.evaluateJavascript(
                            """
                            function kodikMessageListener(message) {
                                console.log(JSON.stringify(message.data));
                                if (message.data.key == 'kodik_player_advert_ended') {
                                   AdHandler.removeAd();
                                }
                            }
                            
                            if (window.addEventListener) {
                                window.addEventListener('message', kodikMessageListener);
                            } else {
                                window.attachEvent('onmessage', kodikMessageListener);
                            }
                        """.trimIndent()
                        ) {}
                        view?.post { clickAdPlay() }
                    } else {
                        this@EmbeddedPlayerActivity.removeAd()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return if ((url.startsWith("http://") || url.startsWith("https://"))) {
                        view.context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        )
                        true
                    } else false
                }
            }
        }

        includedToolbar.gone()
        unlockView.hide()

        pipView.visibleIf { hasPip }

        prev.onClick { presenter.loadPrevEpisode() }
        next.onClick { presenter.loadNextEpisode() }
        rotationView.onClick { toggleOrientation() }
        lockView.onClick { controller.lock() }
        unlockView.onClick { controller.unlock() }
        pipView.onClick { enterPip() }
    }

    private fun removeAd() {
        runOnUiThread {
            adView.gone()
            controller.play()
        }

    }

    private inner class AdInterface() {
        @JavascriptInterface
        fun removeAd() {
            this@EmbeddedPlayerActivity.removeAd()
        }
    }

    private fun clickAdPlay() {
        val d = MotionEvent.obtain(
            150L,
            150L,
            MotionEvent.ACTION_DOWN,
            adView.width / 2f,
            adView.height / 2f,
            0
        )
        adView.dispatchTouchEvent(d)
        val u = MotionEvent.obtain(
            150L,
            150L,
            MotionEvent.ACTION_UP,
            adView.width / 2f,
            adView.height / 2f,
            0
        )
        adView.dispatchTouchEvent(u)
    }

    private fun enterPip() {
        if (hasPip) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) enterPictureInPictureMode()
        }
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (settingsSource.isAutoPip)
            enterPip()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }

    override fun onDestroy() {
        controller.destroy()
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideSystemUi()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (isAutoRotationEnabled) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        playerView.useController = !isInPictureInPictureMode
    }

    private fun toggleOrientation() {
        val orientation = this.resources.configuration.orientation

        when (orientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_USER -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun updateOrientation() {
        val orientation = this.resources.configuration.orientation

        when (orientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_USER -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        if (!controller.isLocked && isAutoRotationEnabled) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun changeBrightness(percent: Int) {
        currentBrightness += percent

        if (currentBrightness > 100) currentBrightness = 100
        else if (currentBrightness < 1) currentBrightness = 1

        val text = "$currentBrightness%"
        showParamChanges(text, brightnessIcon)

        window.attributes = window.attributes.apply { screenBrightness = 2.55f * currentBrightness / 255f }
    }

    private fun changeVolume(percent: Int) {
        val manager = audioManager()

        val max = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume += percent

        if (currentVolume > max) currentVolume = max
        else if (currentVolume < 0) currentVolume = 0

        manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
        val percentVolume = if (currentVolume == 0) 0 else (currentVolume / (max * 0.01f)).roundToInt()
        val text = "$percentVolume%"
        showParamChanges(text, volumeIcon)
    }

    private fun changeProgressSlide(current: Long, max: Long, offset: Long) {
        val validatedOffset = when {
            current + offset < 0 -> current
            current + offset > max -> max - current
            else -> offset
        }

        val validatedCurrent = when {
            current + offset < 0 -> 0
            current + offset > max -> max
            else -> current + offset
        }

        val offsetText = toMinutesAndSecond(validatedOffset)?.let { timeOffset ->
            (if (offset > 0) "+" else "-").plus(timeOffset)
        }
        val maxTimeOffset = toMinutesAndSecond(max)
        val validatedCurrentTimeOffset = toMinutesAndSecond(validatedCurrent)

        if (maxTimeOffset == null || validatedCurrentTimeOffset == null || offsetText == null) return

        val text = "$validatedCurrentTimeOffset / $maxTimeOffset "

        val spanString = SpannableStringBuilder(text)
                .append(offsetText.colorSpan(offsetColor))

        showParamChanges(spanString, null)
    }

    private fun showParamChanges(text: CharSequence, icon: Drawable?) {
        paramChangesView.text = text
        paramChangesView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        paramChangesView.visible()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getLayoutActivity(): Int = R.layout.activity_embedded_player

    override fun getNavigator(): Navigator = Navigator { }

    override fun getNavigatorHolder(): NavigatorHolder = localNavigatorHolder

    override val presenter: EmbeddedPlayerPresenter
        get() = playerPresenter

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun setEpisodeSubtitle(currentEpisode: Int) {
        val subTitle = String.format(getString(R.string.episode_number), currentEpisode)
        toolbar.subtitle = subTitle
    }

    override fun showAd(adLink: String) {
        val iframe =
            "<html><body style='margin:0;padding:0;'><iframe id='kodik-player' src='$adLink' width='100%' height='100%' frameborder='0' allowfullscreen allow='autoplay *; fullscreen *'></iframe></body></html>"
        adView.loadData(iframe, "text/html", "utf-8")
        adView.visible()
    }

    override fun setResolutions(resolutions: List<String>) {
        resolutionSpinnerView.visibleIf { resolutions.isNotEmpty() }
        if (resolutions.isNotEmpty()) {
            resolutionSpinnerView.adapter = ArrayAdapter(this, R.layout.item_spinner_player, resolutions)
            resolutionSpinnerView.setOnItemClickListener { _, _, position, _ -> presenter.onResolutionChanged(resolutions[position]) }
        }
    }

    override fun onBackPressed() {
        super.finish()
    }

    override fun selectTrack(currentTrack: Int) = controller.selectTrack(currentTrack)
    override fun enableNextButton(enable: Boolean) = controller.enableNextButton(enable)
    override fun enablePrevButton(enable: Boolean) = controller.enablePrevButton(enable)
    override fun onShowLoading() = progressBar.visible()
    override fun onHideLoading() = progressBar.gone()
    override fun onShowLightLoading() = bufferingProgressBar.visible()
    override fun onHideLightLoading() = bufferingProgressBar.gone()

    override fun showMessage(s: String, exit: Boolean) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        if (exit) onBackPressed()
    }

    override fun playVideo(
        it: Track,
        subtitles: String?,
        needReset: Boolean,
        headers: Map<String, String>,
        ad: Boolean
    ) {
        val factory = DefaultHttpDataSourceFactory(
            "sap",
            DefaultBandwidthMeter(),
            Constants.LONG_TIMEOUT * 1000,
            Constants.LONG_TIMEOUT * 1000,
            true
        )
        factory.defaultRequestProperties.set(headers)

        val source = MediaSourceHelper
            .withFactory(factory)
            .apply {
                if (it.url.contains("m3u8")) withFormat(VideoFormat.HLS)
                else withFormat(VideoFormat.MP4)
            }
            .withVideoUrl(it.url)
            .withSubtitles(
                subtitles,
                Format.createTextSampleFormat(null, MimeTypes.TEXT_SSA, Format.NO_VALUE, null)
            )
            .get()

        if (needReset) controller.addMediaSource(source, !ad)
        else controller.updateTrack(source)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Player controller
    ///////////////////////////////////////////////////////////////////////////

    private inner class PlayerController(
            private val settingsSource: PlayerSettingsSource
    ) : Player.EventListener, PlayerControlView.VisibilityListener {

        private val player: SimpleExoPlayer

        var isLocked: Boolean = false
        private var controlsVisibility = View.GONE
        private val detector: GestureDetector
        private val scaleDetector: ScaleGestureDetector
        private val gestureListener = ExoPlayerGestureListener()
        private val playerMargin by lazy { dimenAttr(android.R.attr.actionBarSize) }

        private val smallOffset by lazy { settingsSource.forwardRewindOffset }
        private val bigOffset by lazy { settingsSource.forwardRewindOffsetBig }
        private val isGesturesEnabled by lazy { settingsSource.isGesturesEnabled }
        private val isVolumeAndBrightnessGesturesEnabled by lazy { settingsSource.isVolumeAndBrightnessGesturesEnabled }
        private val isVolumeAndBrightnessInverted by lazy { settingsSource.isVolumeAndBrightnessInverted }
        private val isSlideControl by lazy { settingsSource.isForwardRewindSlide }
        private val isZoomProportional by lazy { settingsSource.isZoomProportional }

        private val speedRates = listOf(0.25f, 0.5f, 1f, 1.25f, 1.5f, 2f)
        private var controlsInAction: Boolean = false

        private val connector: MediaSessionConnector

        private val progressListener = object : TimeBar.OnScrubListener {
            private var prevPosition = 0L

            override fun onScrubStart(timeBar: TimeBar?, position: Long) {
                prevPosition = position
                controlsInAction = true
            }

            override fun onScrubStop(timeBar: TimeBar?, position: Long, canceled: Boolean) {
                prevPosition = if (!canceled) position else 0L
                onActionEnd()
            }

            override fun onScrubMove(timeBar: TimeBar?, position: Long) {
                val dragText = (if (prevPosition < position) "+" else "-").plus(" ${toMinutesAndSecond(prevPosition - position)}")
                dragProgress.apply {
                    text = dragText
                    visible()
                    removeCallbacks(dragPostHideRunnable)
                    postDelayed(dragPostHideRunnable, CONTROLLER_HIDE_DELAY)
                }
            }
        }

        private val spinnerOpenListener = object : CustomSpinner.OnSpinnerEventsListener {
            override fun onSpinnerOpened(spinner: ReSpinner?) {
                controlsInAction = true
            }

            override fun onSpinnerClosed(spinner: ReSpinner?) = onActionEnd()
        }

        init {
            val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory())
            player = ExoPlayerFactory.newSimpleInstance(this@EmbeddedPlayerActivity, trackSelector)
            player.addListener(this)
            playerView.player = player
            playerView.setControllerShowOnTouch(false)
            playerView.setControllerVisibilityListener(this)
            playerView.controllerAutoShow = false
            detector = GestureDetector(this@EmbeddedPlayerActivity, gestureListener)
            scaleDetector = ScaleGestureDetector(this@EmbeddedPlayerActivity, gestureListener)
            playerView.setOnTouchListener(gestureListener)
            exo_progress.addListener(progressListener)
            resolutionSpinnerView.setSpinnerEventsListener(spinnerOpenListener)
            speedSpinnerView.setSpinnerEventsListener(spinnerOpenListener)

            val mediaSession = MediaSessionCompat(this@EmbeddedPlayerActivity, packageName)
            connector = MediaSessionConnector(mediaSession)
        }

        val isVisible
            get() = controlsVisibility == View.VISIBLE

        fun enableNextButton(enable: Boolean) {
            val alpha =
                    if (!enable) 0.3f
                    else 1f
            next.alpha = alpha
            next.isEnabled = enable
        }

        fun enablePrevButton(enable: Boolean) {
            val alpha =
                    if (!enable) 0.3f
                    else 1f
            prev.alpha = alpha
            prev.isEnabled = enable
        }

        fun selectTrack(currentTrack: Int) {
            resolutionSpinnerView.setSelection(currentTrack, false)
        }

        fun changePlaySpeed(currentSpeed: Int) {
            player.playbackParameters = PlaybackParameters(speedRates[currentSpeed])
            speedSpinnerView.setSelection(currentSpeed, false)
        }

        fun addMediaSource(source: MediaSource?, playWhenReady : Boolean) {
            player.playWhenReady = playWhenReady
            player.prepare(source)
        }

        fun updateTrack(source: MediaSource?) {
            player.playWhenReady = true
            player.prepare(source, false, false)
        }

        fun onStart() {
            connector.setPlayer(player, null)
            connector.mediaSession.isActive = true
        }

        fun onStop() {
            player.playWhenReady = false
            connector.setPlayer(null, null)
            connector.mediaSession.isActive = false
        }

        fun destroy() {
            player.stop()
            player.release()
            connector.mediaSession.release()
        }

        fun lock() {
            isLocked = true
            updateOrientation()
            onLockedScreenTouch()
        }

        fun unlock() {
            isLocked = false
            TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_OUT))
            unlockSurface.gone()
            unlockView.hide()
            updateOrientation()
        }

        fun onFastForward(): Boolean {
            forwardView.text = smallOffsetText
            seek(smallOffset)
            hideForwardAfterTimeout()
            return true
        }

        fun onRewind(): Boolean {
            rewindView.text = smallOffsetText
            seek(-smallOffset)
            hideRewindAfterTimeout()
            return true
        }

        fun onBigForward() {
            forwardView.text = bigOffsetText
            seek(bigOffset)
            hideForwardAfterTimeout()
        }

        fun onBigRewind() {
            rewindView.text = bigOffsetText
            seek(-bigOffset)
            hideRewindAfterTimeout()
        }

        fun play() {
            player.playWhenReady = true
        }

        private fun seek(pos: Long) {
            var mills = pos
            if (mills >= 0 || player.currentPosition != 0L) {
                mills += player.currentPosition

                if (mills < 0) mills = 0
                if (mills > player.duration) mills = player.duration

                player.seekTo(mills)
            }
        }

        override fun onVisibilityChange(visibility: Int) {
            controlsVisibility = visibility

            if (isVisible && !controlsInAction) {
                hideAfterTimeout()
                includedToolbar.visible()
            } else if (!controlsInAction) {
                includedToolbar.gone()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            error?.printStackTrace()
            showMessage(getString(R.string.player_error))
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> onShowLightLoading()
                else -> onHideLightLoading()
            }
        }

        private fun toggleControllerVisibility(): Boolean {
            if (isVisible && !controlsInAction) playerView.hideController()
            else if (!controlsInAction) playerView.showController()
            return true
        }

        private fun onActionEnd() {
            controlsInAction = false
            hideAfterTimeout()
        }

        private fun onLockedScreenTouch(): Boolean {
            unlockView.show()
            TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_IN))
            unlockSurface.visible()
            playerView.hideController()
            hideUnlockAfterTimeout()
            return false
        }

        private fun hideForwardAfterTimeout() {
            forwardView.apply {
                removeCallbacks(delayedForwardHide)
                visible()
                postDelayed(delayedForwardHide, FORWARD_REWIND_HIDE_DELAY)
            }
        }

        private fun hideRewindAfterTimeout() {
            rewindView.apply {
                removeCallbacks(delayedRewindHide)
                visible()
                postDelayed(delayedRewindHide, FORWARD_REWIND_HIDE_DELAY)
            }
        }

        private fun hideAfterTimeout() {
            playerView.removeCallbacks(postHideRunnable)
            playerView.postDelayed(postHideRunnable, CONTROLLER_HIDE_DELAY)
        }

        private fun hideUnlockAfterTimeout() {
            unlockView.removeCallbacks(delayedUnlockHide)
            unlockView.postDelayed(delayedUnlockHide, UNLOCK_HIDE_DELAY)
        }

        private val postHideRunnable = Runnable { if (!controlsInAction) playerView.hideController() }
        private val delayedForwardHide = Runnable { forwardView.gone() }
        private val delayedRewindHide = Runnable { rewindView.gone() }
        private val delayedUnlockHide = Runnable { unlockView.hide(); TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_OUT)); unlockSurface.gone() }
        private val delayedParamChangesHide = Runnable { TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_OUT)); paramChangesView.gone() }
        private val dragPostHideRunnable = Runnable { dragProgress.gone(); dragProgress.text = null }

        private inner class ExoPlayerGestureListener : GestureDetector.SimpleOnGestureListener(), View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

            private var isDrag: Boolean = false
            private var isSlide: Boolean = false
            private var slideOffset = 0L
            private var lastTapMills = 0L
            private var scaleFactor = 0.5f
            private var scrollFirstTapX = 0

            private val MOVEMENT_TH = 30
            private val stepBrightness = 5
            private val stepVolume = 1

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event == null || event.y < playerMargin / 2 || event.x > playerView.width - playerMargin) return false

                return if (!isLocked) {
                    if (event.action == MotionEvent.ACTION_UP && isDrag) {
                        isDrag = false
                        onScrollEnd()
                    }

                    if (isGesturesEnabled) kotlin.run { scaleDetector.onTouchEvent(event); detector.onTouchEvent(event) }
                    else {
                        when {
                            (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN) && System.currentTimeMillis() - lastTapMills > 500 -> {
                                lastTapMills = System.currentTimeMillis()
                                toggleControllerVisibility()
                            }
                            else -> false
                        }
                    }
                } else onLockedScreenTouch()
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                return toggleControllerVisibility()
            }

            override fun onLongPress(e: MotionEvent?) {
                if (e == null || isSlideControl) return

                when {
                    e.x > playerView.width - playerView.width / 3 -> onBigForward()
                    e.x < playerView.width / 3 -> onBigRewind()
                }
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (e == null || isSlideControl) return false

                return when {
                    e.x > playerView.width - playerView.width / 3 -> onFastForward()
                    e.x < playerView.width / 3 -> onRewind()
                    else -> false
                }
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                if (e1 == null || e2 == null) return false

                return if (isVolumeAndBrightnessGesturesEnabled) {
                    val diff = Math.abs(e1.y - e2.y).toInt()
                    isDrag = true

                    val stepChanged = Math.abs(distanceY).roundToInt() % 3 == 0 && diff > MOVEMENT_TH

                    if (stepChanged && (e1.y > playerMargin && e2.y > playerMargin)) when {
                        e1.x < playerView.width / 3 -> leftAreaScroll(distanceY > 0)
                        e1.x > playerView.width - playerView.width / 3 -> rightAreaScroll(distanceY > 0)
                        else -> false
                    }
                    else false

                } else if (isSlideControl) {
                    slideOffset = -(scrollFirstTapX - e2.x).toInt() * 100L
                    isDrag = true
                    isSlide = true

                    changeProgressSlide(player.currentPosition, player.duration, slideOffset)
                    Log.i("DEVE", " $slideOffset")
                    scrollFirstTapX = e1.x.toInt()

                    return true
                } else false
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                scaleFactor *= scaleDetector.scaleFactor

                scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 1.0f))

                if (scaleFactor > 0.5f) playerView.resizeMode = if (isZoomProportional) AspectRatioFrameLayout.RESIZE_MODE_ZOOM else AspectRatioFrameLayout.RESIZE_MODE_FILL
                else playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean = true
            override fun onScaleEnd(detector: ScaleGestureDetector?) = Unit

            private fun onScrollEnd() {
                paramChangesView.removeCallbacks(delayedParamChangesHide)
                paramChangesView.postDelayed(delayedParamChangesHide, UNLOCK_HIDE_DELAY)

                if (isSlide) {
                    seek(slideOffset)
                    isSlide = false
                    slideOffset = 0L
                    scrollFirstTapX = 0
                }
            }

            private fun leftAreaScroll(increasing: Boolean): Boolean {
                if (isVolumeAndBrightnessInverted) changeBrightness(if (increasing) stepBrightness else -stepBrightness)
                else changeVolume(if (increasing) stepVolume else -stepVolume)
                return true
            }

            private fun rightAreaScroll(increasing: Boolean): Boolean {
                if (isVolumeAndBrightnessInverted) changeVolume(if (increasing) stepVolume else -stepVolume)
                else changeBrightness(if (increasing) stepBrightness else -stepBrightness)
                return true
            }
        }
    }

    private val isAutoRotationEnabled: Boolean
        get() = android.provider.Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1

    private fun toMinutesAndSecond(value: Long): String? {
        if (value.absoluteValue !in 0..Int.MAX_VALUE) return null

        return Duration.millis(Math.abs(value)).toMinutesAndSeconds()
    }

    private val hasPip: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)

}