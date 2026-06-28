package com.gnoemes.shikimori.presentation.view.screenshots

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ActivityScreenshotsBinding
import com.gnoemes.shikimori.entity.anime.domain.ScreenshotsNavigationData
import com.gnoemes.shikimori.presentation.view.base.activity.MvpActivity
import com.gnoemes.shikimori.presentation.view.screenshots.adapter.ScreenshotPagerAdapter
import com.gnoemes.shikimori.utils.*


class ScreenshotsActivity : MvpActivity() {

    companion object {
        private const val CURRENT_PAGE = "CURRENT_PAGE"
        private const val UI_VISIBLE = "UI_VISIBLE"
        private const val SCREENSHOTS_DATA_KEY = "SCREENSHOTS_DATA_KEY"
        fun newIntent(context: Context?, data: ScreenshotsNavigationData): Intent {
            val intent = Intent(context, ScreenshotsActivity::class.java)
            intent.putExtra(SCREENSHOTS_DATA_KEY, data)
            return intent
        }
    }

    private var _binding: ActivityScreenshotsBinding? = null
    private val binding: ActivityScreenshotsBinding? get() = _binding

    private var adapter: ScreenshotPagerAdapter? = null
    private var itemCount = 0
    private val formatString by lazy { getString(R.string.common_count_format) }
    private var uiVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ShikimoriAppTheme_Screenshots)
        theme.applyStyle(getCurrentAscentTheme, true)
        super.onCreate(savedInstanceState)
        _binding = ActivityScreenshotsBinding.inflate(layoutInflater)
        val b = _binding ?: return
        setContentView(b.root)

        b.toolbar.run {
            addBackButton(R.drawable.ic_close) { finish() }
            inflateMenu(R.menu.menu_screenshots)
            onMenuClick {
                when (it?.itemId) {
                    R.id.item_share -> share(getCurrentScreenshot())
                    R.id.item_download -> download(getCurrentScreenshot())
                }
                true
            }
        }
        with(b.viewpager) {
            offscreenPageLimit = 5
            addOnPageChangeListener(pageChangeCallback)
        }

        if (intent != null) {
            val data: ScreenshotsNavigationData? = intent.getParcelableExtra(SCREENSHOTS_DATA_KEY, ScreenshotsNavigationData::class.java)
            if (data != null) {
                adapter = ScreenshotPagerAdapter(data.items, this::toggleUI, this::onSwipe, this::onDismiss)
                itemCount = data.items.size
                val pos = savedInstanceState?.getInt(CURRENT_PAGE, data.selected) ?: data.selected
                b.viewpager.adapter = this@ScreenshotsActivity.adapter
                b.viewpager.setCurrentItem(pos, false)
                b.toolbar.title = String.format(formatString, pos + 1, data.items.size)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(b.appBarLayout) { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, insets.systemWindowInsetRight, 0)
            insets
        }

        uiVisible = savedInstanceState?.getBoolean(UI_VISIBLE, true) ?: true
        if (uiVisible) showUi()
        else hideUi()
    }

    private fun getCurrentScreenshot(): String? {
        val b = _binding ?: return null
        val adapter = b.viewpager.adapter as? ScreenshotPagerAdapter
        val items = adapter?.items
        return items?.getOrNull(b.viewpager.currentItem)?.original
    }

    private fun onSwipe() {
        hideUi(false)
    }

    private fun onDismiss() {
        finish()
        overridePendingTransition(0, 0)
    }

    private fun share(url: String?) {
        val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, url) }, getString(R.string.common_share))
        startActivity(intent)
    }

    private fun download(url: String?) {
        val file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val name = url?.let { it.substring(it.lastIndexOf("/") + 1) }
        val path = Uri.withAppendedPath(Uri.fromFile(file), name)

        val manager = downloadManager()
        val request = DownloadManager.Request(url?.toUri())
                .setTitle(name)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .apply {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        @Suppress("DEPRECATION")
                        allowScanningByMediaScanner()
                    }
                    setDestinationUri(path)
                }

        manager?.enqueue(request)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MediaScannerConnection.scanFile(this, arrayOf(path.path), null, null)
        }
    }

    private fun toggleUI() {
        if (uiVisible) hideUi()
        else showUi()
        uiVisible = !uiVisible
    }

    private fun showUi() {
        val b = _binding ?: return
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
        TransitionManager.beginDelayedTransition(b.coordinator, Fade().apply { duration = 110 })
        b.appBarLayout.visible()
    }


    private fun hideUi(animate : Boolean = true) {
        val b = _binding ?: return
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        if (animate) TransitionManager.beginDelayedTransition(b.coordinator, Fade().apply { duration = 110 })
        b.appBarLayout.gone()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding?.viewpager?.removeOnPageChangeListener(pageChangeCallback)
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(CURRENT_PAGE, _binding?.viewpager?.currentItem ?: 0)
        outState.putBoolean(UI_VISIBLE, uiVisible)
    }

    private val pageChangeCallback = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            _binding?.toolbar?.title = String.format(formatString, position + 1, itemCount)
        }
    }
}