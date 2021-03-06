package com.gnoemes.shikimori.presentation.view.base.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.utils.getThemeSharedPreferences
import com.gnoemes.shikimori.utils.setTheme
import com.gnoemes.shikimori.utils.themeHandler


abstract class BaseThemedActivity : MvpActivity() {

    protected open var applyTheme: Boolean = true
    private val recreateRunnable by lazy { Runnable { recreate() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (applyTheme) setTheme(recreateRunnable)
        super.onCreate(savedInstanceState)
        getThemeSharedPreferences().registerOnSharedPreferenceChangeListener(themeChangeListener)
    }

    protected open val themeChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == ThemeExtras.THEME_KEY || key == ThemeExtras.ASCENT_KEY || key == ThemeExtras.NIGHT_THEME_KEY || key == ThemeExtras.NIGHT_THEME_START_KEY || key == ThemeExtras.NIGHT_THEME_END_KEY) {
            finish()
            val intent = intent
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getThemeSharedPreferences().unregisterOnSharedPreferenceChangeListener(themeChangeListener)
        themeHandler.removeCallbacks(recreateRunnable)
    }
}