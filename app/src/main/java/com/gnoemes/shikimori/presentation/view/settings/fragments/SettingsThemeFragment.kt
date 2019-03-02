package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.utils.getThemeSharedPreferences
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.putInt

class SettingsThemeFragment : BaseSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(ThemeExtras.THEME_KEY)?.apply {
            summary = getLocalizedTheme(prefs().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default))
            onPreferenceClickListener = themeClickListener
        }
    }

    private val themeClickListener = Preference.OnPreferenceClickListener { preference ->
        showListDialog(R.array.themes) { _, index, text ->
            when (index) {
                0 -> prefs().putInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default)
                1 -> prefs().putInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Dark)
            }
            preference.summary = text
        }
    }

    private fun getLocalizedTheme(style: Int): String = when (style) {
        R.style.ShikimoriAppTheme_Dark -> getString(R.string.theme_dark)
        else -> getString(R.string.theme_default)
    }

    private fun PreferenceFragmentCompat.prefs(): SharedPreferences = context!!.getThemeSharedPreferences()


    override val preferenceScreen: Int
        get() = R.xml.preferences_theme
}