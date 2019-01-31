package com.gnoemes.shikimori.presentation.view.player.embedded

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
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
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.exoplayer.MediaSourceHelper
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_embedded_player.*
import kotlinx.android.synthetic.main.layout_player_bottom.*
import kotlinx.android.synthetic.main.layout_player_controls.*
import kotlinx.android.synthetic.main.layout_player_rewind_forward.*
import kotlinx.android.synthetic.main.layout_player_toolbar.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

//TODO create custom spinner and listener for open/close events
class EmbeddedPlayerActivity : BaseActivity<EmbeddedPlayerPresenter, EmbeddedPlayerView>(), EmbeddedPlayerView {

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

    private val controller by lazy { PlayerController() }

    private val smallOffsetText by lazy { "${settingsSource.forwardRewindOffset / 1000} ${getString(R.string.player_seconds_short)}" }
    private val bigOffsetText by lazy { "${settingsSource.forwardRewindOffsetBig / 1000} ${getString(R.string.player_seconds_short)}" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        toolbar.addBackButton { onBackPressed() }
        toolbar.navigationIcon?.tint(baseContext.color(R.color.player_controls))

        if (settingsSource.isOpenLandscape) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        forwardView.text = smallOffsetText
        rewindView.text = smallOffsetText

        exo_progress.setBufferedColor(ColorUtils.setAlphaComponent(colorAttr(R.attr.colorAccentTransparent), 153))
        resolutionSpinnerView.setPopupBackgroundResource(R.drawable.background_player_resolution)
        resolutionSpinnerView.background.tint(color(R.color.player_controls))
        includedToolbar.gone()
        unlockView.hide()

        prev.onClick { presenter.loadPrevEpisode() }
        next.onClick { presenter.loadNextEpisode() }
        rotationView.onClick { toggleOrientation() }
        lockView.onClick { controller.lock() }
        unlockView.onClick { controller.unlock() }
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

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (isAutoRotationEnabled) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
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

    override fun playVideo(it: Track, needReset: Boolean) {
        Log.i("PLAYER", "loading: ${it.url}")
        val source = MediaSourceHelper
                .withFactory(DefaultHttpDataSourceFactory("sap", DefaultBandwidthMeter(), Constants.LONG_TIMEOUT * 1000, Constants.LONG_TIMEOUT * 1000, true))
                .withFormat(VideoFormat.MP4)
                .withVideoUrl(it.url)
                .get()

        if (needReset) controller.addMediaSource(source)
        else controller.updateTrack(source)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Player controller
    ///////////////////////////////////////////////////////////////////////////

    private inner class PlayerController() : Player.EventListener, PlayerControlView.VisibilityListener {

        private val player: SimpleExoPlayer

        var isLocked: Boolean = false
        private var controlsVisibility = View.GONE
        private val detector: GestureDetector
        private val gestureListener = ExoPlayerGestureListener()

        //TODO move to constructor?
        private val smallOffset by lazy { settingsSource.forwardRewindOffset }
        private val bigOffset by lazy { settingsSource.forwardRewindOffsetBig }
        private val isGesturesEnabled by lazy { settingsSource.isGesturesEnabled }
        private val isVolumeAndBrighnessGesturesEnabled by lazy { settingsSource.isVolumeAndBrightnessGesturesEnabled }
        private val isVolumeAndBrightnessInverted by lazy { settingsSource.isVolumeAndBrightnessInverted }
        private val isSlideControl by lazy { settingsSource.isForwardRewindSlide }

        init {
            val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory())
            player = ExoPlayerFactory.newSimpleInstance(this@EmbeddedPlayerActivity, trackSelector)
            player.addListener(this)
            playerView.player = player
            playerView.setControllerShowOnTouch(false)
            playerView.setControllerVisibilityListener(this)
            playerView.controllerAutoShow = false
            detector = GestureDetector(this@EmbeddedPlayerActivity, gestureListener)
            playerView.setOnTouchListener(gestureListener)
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

        fun addMediaSource(source: MediaSource?) {
            player.playWhenReady = true
            player.prepare(source)
        }

        fun updateTrack(source: MediaSource?) {
            player.playWhenReady = true
            player.prepare(source, false, false)
        }

        fun onStop() {
            player.playWhenReady = false
        }

        fun destroy() {
            player.stop()
            player.release()
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

        fun onFastForward() {
            forwardView.text = smallOffsetText
            seek(smallOffset)
            hideForwardAfterTimeout()
        }

        fun onRewind() {
            rewindView.text = bigOffsetText
            seek(-smallOffset)
            hideRewindAfterTimeout()
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

        private fun seek(pos: Long) {
            var mills = pos
            if (mills >= 0 || player.currentPosition != 0L) {
                mills += player.currentPosition

                if (mills < 0) mills = 0

                player.seekTo(mills)
            }
        }

        override fun onVisibilityChange(visibility: Int) {
            controlsVisibility = visibility

            if (isVisible) {
                hideAfterTimeout()
                TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_IN))
                includedToolbar.visible()
            } else {
                TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_OUT))
                includedToolbar.gone()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            error?.printStackTrace()
            //TODO process player error (404 on VK)
            super.onPlayerError(error)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> onShowLightLoading()
                else -> onHideLightLoading()
            }
        }

        private fun toggleControllerVisibility(): Boolean {
            if (isVisible) playerView.hideController()
            else playerView.showController()
            return true
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

        private val postHideRunnable = Runnable { playerView.hideController() }
        private val delayedForwardHide = Runnable { forwardView.gone() }
        private val delayedRewindHide = Runnable { rewindView.gone() }
        private val delayedUnlockHide = Runnable { unlockView.hide(); TransitionManager.beginDelayedTransition(container, Fade(Fade.MODE_OUT)); unlockSurface.gone() }

        private inner class ExoPlayerGestureListener : GestureDetector.SimpleOnGestureListener(), View.OnTouchListener {

            private var isDrag: Boolean = false
            private var lastTapMills = 0L

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return if (!isLocked) {
                    if (event?.action == MotionEvent.ACTION_UP && isDrag) {
                        isDrag = false
                        onScrollEnd()
                    }

                    if (isGesturesEnabled) detector.onTouchEvent(event)
                    else {
                        when {
                            (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_DOWN) && System.currentTimeMillis() - lastTapMills > 500 -> {
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

            override fun onLongPress(e: MotionEvent) {
                if (e.x > playerView.width / 2) onBigForward()
                else onBigRewind()
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                return if (!isSlideControl) {
                    if (e.x > playerView.width / 2) onFastForward()
                    else onRewind()
                    true
                } else false
            }

            private fun onScrollEnd() {
                //TODO remove and add volume/brightness callbacks
            }
        }
    }

    private val isAutoRotationEnabled: Boolean
        get() = android.provider.Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
}