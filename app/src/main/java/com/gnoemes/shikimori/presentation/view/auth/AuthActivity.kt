package com.gnoemes.shikimori.presentation.view.auth

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.auth.AuthType
import com.gnoemes.shikimori.presentation.presenter.auth.AuthPresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseActivity
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.activity_auth.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import java.util.regex.Pattern
import javax.inject.Inject

class AuthActivity : BaseActivity<AuthPresenter, AuthView>(), AuthView {

    @Inject
    lateinit var localNavigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter {
        authPresenter = presenterProvider.get()
        intent.ifNotNull {
            authPresenter.authType = it.getSerializableExtra(AppExtras.ARGUMENT_AUTH_TYPE) as AuthType
        }

        return authPresenter
    }

    private val client by lazy { ShikimoriAuthClient() }


    companion object {
        private const val PATTERN = "https?://(?:www\\.)?shikimori\\.one/oauth/authorize/(?:.*)"
        private const val SIGN_UP_URL = "https://shikimori.one/users/sign_up"
        private const val SIGN_IN_URL = "https://shikimori.one/users/sign_in"
        fun newIntent(context: Context?, type: AuthType) = Intent(context, AuthActivity::class.java)
                .apply { putExtra(AppExtras.ARGUMENT_AUTH_TYPE, type) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
//        webView = WebView(this)
//        webView.setTag(R.id.aesthetic_ignore, "webView")
//        container.addView(webView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        webView.apply {
            settings.setAppCacheEnabled(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.databaseEnabled = false
            settings.domStorageEnabled = false
            settings.javaScriptEnabled = true
            webViewClient = client
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
        webView.loadUrl(SIGN_IN_URL)
    }

    override fun onSignUp() {
        webView.loadUrl(SIGN_UP_URL)
    }

    override fun setTitle(title: String) {}

    private inner class ShikimoriAuthClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            interceptCode(url)
            return false
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
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
            val matcher = Pattern.compile(PATTERN).matcher(url)
            if (matcher.find()) {
                val authCode =
                        if (matcher.group().isNullOrEmpty()) ""
                        else url!!.substring(
                                url.lastIndexOf("/"))
                                .replaceFirst("/", "")
                presenter.onAuthCodeReceived(authCode)

                webView.gone()
                progressBar.visible()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}