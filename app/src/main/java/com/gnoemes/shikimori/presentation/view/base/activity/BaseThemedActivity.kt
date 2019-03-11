package com.gnoemes.shikimori.presentation.view.base.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.utils.getCurrentTheme
import com.gnoemes.shikimori.utils.getThemeSharedPreferences



abstract class BaseThemedActivity : MvpActivity() {

    protected open var applyTheme : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (applyTheme) setTheme(getCurrentTheme)
        super.onCreate(savedInstanceState)
        getThemeSharedPreferences().registerOnSharedPreferenceChangeListener(themeChangeListener)
    }

    private val themeChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == ThemeExtras.THEME_KEY) {
            finish()
            val intent = intent
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getThemeSharedPreferences().unregisterOnSharedPreferenceChangeListener(themeChangeListener)
    }
}