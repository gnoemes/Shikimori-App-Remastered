package com.gnoemes.shikimori.presentation.view.auth

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.auth.AuthType
import com.gnoemes.shikimori.presentation.presenter.auth.AuthPresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseActivity
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.putString
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.activity_auth.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import java.util.regex.Pattern
import javax.inject.Inject

//TODO split screens/refactor/in-app browser
class AuthActivity : BaseActivity<AuthPresenter, AuthView>(), AuthView {

    @Inject
    lateinit var localNavigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter {
        authPresenter = presenterProvider.get()
        intent.ifNotNull {
            authPresenter.authType = it.getSerializableExtra(AppExtras.ARGUMENT_AUTH_TYPE) as? AuthType
        }

        return authPresenter
    }

    private val shikimoriClient by lazy { ShikimoriAuthClient() }
    private val anime365Client by lazy { Anime365AuthClient() }


    companion object {
        private const val SHIKIMORI_PATTERN_OLD =
            "https?://(?:www\\.)?shikimori\\.me/oauth/authorize/(?:.*)"
        private const val SHIKIMORI_PATTERN =
            "https?://(?:www\\.)?shikimori\\.one/oauth/authorize/(?:.*)"
        private const val SHIKIMORI_SIGN_UP_URL = "https://shikimori.one/users/sign_up"
        private const val SHIKIMORI_SIGN_IN_URL = "https://shikimori.one/users/sign_in"

        private const val ANIME_365_SIGN_IN = "https://smotret-anime.com/users/login"

        fun shikimoriAuth(context: Context?, type: AuthType) =
            Intent(context, AuthActivity::class.java)
                .apply { putExtra(AppExtras.ARGUMENT_AUTH_TYPE, type) }

        fun anime365Auth(context: Context?) = Intent(context, AuthActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shikimori = intent?.getSerializableExtra(AppExtras.ARGUMENT_AUTH_TYPE) != null
        initWebView(shikimori)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(shikimori: Boolean) {
        webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = if (shikimori) shikimoriClient else anime365Client
        }
    }

    override fun onDestroy() {
        webView.webViewClient = null
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override val presenter: AuthPresenter
        get() = authPresenter

    override fun getLayoutActivity(): Int = R.layout.activity_auth

    override fun getNavigator(): Navigator = Navigator { }

    override fun getNavigatorHolder(): NavigatorHolder = localNavigatorHolder

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun onSignIn() {
        webView.loadUrl(SHIKIMORI_SIGN_IN_URL)
    }

    override fun onSignUp() {
        webView.loadUrl(SHIKIMORI_SIGN_UP_URL)
    }

    override fun onAnime365() {
        webView.loadUrl(ANIME_365_SIGN_IN)
    }

    override fun setTitle(title: String) {}

    private inner class ShikimoriAuthClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            interceptCode(url)
            return false
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            interceptCode(request?.url?.toString())
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visible()
            if (url == BuildConfig.ShikimoriBaseUrl) {
                view?.loadUrl(Constants.AUTH_URL)
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.gone()
        }

        private fun interceptCode(url: String?) {
            val matcherFixed = Pattern.compile(SHIKIMORI_PATTERN).matcher(url)
            val matcherOld = Pattern.compile(SHIKIMORI_PATTERN_OLD).matcher(url)
            val matcher = when {
                matcherFixed.find() -> matcherFixed
                matcherOld.find() -> matcherOld
                else -> null
            }
            if (matcher != null) {
                val authCode =
                    if (matcher.group().isNullOrEmpty()) ""
                    else url!!.substring(
                        url.lastIndexOf("/")
                    )
                        .replaceFirst("/", "")
                presenter.onAuthCodeReceived(authCode)

                webView.gone()
                progressBar.visible()
            }
        }
    }

    private inner class Anime365AuthClient : WebViewClient() {

        //TODO remove
        private var finishCounter = 0
        private val TOKEN_URL = "https://smotret-anime.com/api/accessToken?app=sapp"


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visible()
            if (url == "https://smotret-anime.com/" || url == "https://smotret-anime.com/users/profile") {
                view?.loadUrl(TOKEN_URL)
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            //TODO remove
            if (url == ANIME_365_SIGN_IN) finishCounter++

            if (finishCounter > 1) {
                finishCounter = 0
                view?.postDelayed({
                    view.loadUrl("https://smotret-anime.com/")
                }, 750)
            }

            interceptCode(url)
            progressBar.gone()
        }

        private fun interceptCode(url: String?) {
            if (url == TOKEN_URL) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("(function() { return JSON.stringify(document.getElementsByTagName('html')[0].innerHTML); })();") { s ->
                        processCode(s)
                    }
                } else {
                    //TODO remove, add js interface
                    Toast.makeText(
                        applicationContext,
                        "Авторизация на вашей версии Android временно не возможна",
                        Toast.LENGTH_LONG
                    ).show()
                    onBackPressed()
                }
                webView.gone()
                progressBar.visible()
            }
        }

        private fun processCode(html: String) {
            val matcher = Pattern.compile("\\{.*\\}").matcher(html)
            if (matcher.find()) {
                val code = matcher
                    .group()
                    .replace("\\", "")
                    .split("\"")
                    .takeLast(2)
                    .firstOrNull()

                if (!code.isNullOrBlank()) onSuccess(code)
            }
        }

        private fun onSuccess(code: String) {
            getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                .putString(SettingsExtras.ANIME_365_TOKEN, code)
            Toast.makeText(
                applicationContext,
                R.string.settings_anime_365_success,
                Toast.LENGTH_LONG
            ).show()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}