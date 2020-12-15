package com.gnoemes.shikimori.presentation.view.player.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.presentation.view.base.activity.BaseThemedActivity
import com.gnoemes.shikimori.utils.Utils
import com.gnoemes.shikimori.utils.widgets.VideoWebChromeClient
import kotlinx.android.synthetic.main.activity_web_player.*
import java.util.regex.Pattern

class WebPlayerActivity : BaseThemedActivity() {

    private lateinit var chromeClient: VideoWebChromeClient
    private lateinit var webView: WebView

    companion object {
        private val ANIME_365_REGEX = "smotret-anime\\.online".toRegex()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_web_player)

        showNoAdsMessage()

        webView = WebView(applicationContext)
        frame.addView(webView)

        chromeClient = VideoWebChromeClient(webView, windowCallback)
        webView.apply {
            webChromeClient = chromeClient
            webViewClient = client
            setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                setAppCacheEnabled(true)
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                javaScriptCanOpenWindowsAutomatically = true
                javaScriptEnabled = true
                domStorageEnabled = true
                allowContentAccess = true
                allowUniversalAccessFromFileURLs = true
                userAgentString = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }

        if (intent != null) {
            val url = intent.getStringExtra(AppExtras.ARGUMENT_URL)
            if (!url.isNullOrBlank()) {
                val prefs = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                val token = prefs.getString(SettingsExtras.ANIME_365_TOKEN, null)

                val useIFrame = Utils.checkNeedIFrame(url)

                if (url.contains(ANIME_365_REGEX) && !token.isNullOrBlank()) webView.loadUrl("$url?access_token=$token")
                else if (useIFrame) {
                    val iframe = "<html><body style='margin:0;padding:0;'><iframe src='$url' width='100%' height='100%'  frameborder='0' allowfullscreen></iframe></body></html>"
                    webView.loadData(iframe, "text/html", "utf-8")
                } else webView.loadUrl(url)

            } else showError()
        } else onBackPressed()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) hideSystemUi()
            else showSystemUI()
        }
    }

    private fun showNoAdsMessage() {
        val text = getString(R.string.player_no_ads)
        showMessage(text)
    }

    private fun showError() {
        val text = getString(R.string.player_error)
        showMessage(text)
    }

    private fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.finish()
    }

    override fun onDestroy() {
        frame?.removeAllViews()
        window.decorView.destroyDrawingCache()
        webView.webChromeClient = null
        webView.webViewClient = null
        webView.destroy()
        super.onDestroy()
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun hideSystemUi() {
        window.decorView
                .systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private val client = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                if (Pattern.compile("https?://vk\\.com/").matcher(url).find()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                } else false

            } else super.shouldOverrideUrlLoading(view, url)
        }
    }

    private val windowCallback = object : WindowCallback {
        override fun onFullscreenMode() = window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        override fun onNormalMode() = window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    interface WindowCallback {
        fun onFullscreenMode()

        fun onNormalMode()
    }

}