package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.presentation.view.common.widget.preferences.AscentPreference
import com.gnoemes.shikimori.presentation.view.common.widget.preferences.ThemePreference
import com.gnoemes.shikimori.presentation.view.settings.ToolbarCallback
import com.gnoemes.shikimori.utils.getThemeSharedPreferences
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.putInt

class SettingsThemeFragment : BaseSettingsFragment(), Toolbar.OnMenuItemClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        (activity as? ToolbarCallback)?.showToolbarMenu()
        themePreference.setTheme(prefs().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default))
        ascentPreference.setAscentStyle(prefs().getInt(ThemeExtras.ASCENT_KEY, R.style.AscentStyle_Orange))
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_accept) saveTheme()
        return true
    }

    private fun saveTheme() {
        prefs().putInt(ThemeExtras.THEME_KEY, themePreference.getTheme())
        prefs().putInt(ThemeExtras.ASCENT_KEY, ascentPreference.getAscentStyle())
        activity?.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? ToolbarCallback)?.hideToolbarMenu()
    }

    private fun PreferenceFragmentCompat.prefs(): SharedPreferences = context!!.getThemeSharedPreferences()

    private val themePreference: ThemePreference
        get() = preference(ThemeExtras.THEME_KEY) as ThemePreference

    private val ascentPreference: AscentPreference
        get() = preference(ThemeExtras.ASCENT_KEY) as AscentPreference

    override val preferenceScreen: Int
        get() = R.xml.preferences_theme
}