package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.timePicker
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.presentation.view.common.widget.preferences.AscentPreference
import com.gnoemes.shikimori.presentation.view.common.widget.preferences.ThemePreference
import com.gnoemes.shikimori.presentation.view.settings.ToolbarCallback
import com.gnoemes.shikimori.utils.getThemeSharedPreferences
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.putInt
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalTime

class SettingsThemeFragment : BaseSettingsFragment(), Toolbar.OnMenuItemClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        (activity as? ToolbarCallback)?.showToolbarMenu()
        themePreference.apply {
            setTheme(prefs().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default))
            setNightTheme(prefs().getInt(ThemeExtras.NIGHT_THEME_KEY, -1))
            setNightModeStartTime(prefs().getInt(ThemeExtras.NIGHT_THEME_START_KEY, LocalTime(20, 0, 0, 0).millisOfDay))
            setNightModeEndTime(prefs().getInt(ThemeExtras.NIGHT_THEME_END_KEY, LocalTime(8, 0, 0, 0).millisOfDay))
            nightTimeStartClickListener = View.OnClickListener { showTimeDialog(getNightModeStartTime()) { setNightModeStartTime(it) } }
            nightTimeEndClickListener = View.OnClickListener { showTimeDialog(getNightModeEndTime()) { setNightModeEndTime(it) } }
        }

        ascentPreference.setAscentStyle(prefs().getInt(ThemeExtras.ASCENT_KEY, R.style.AscentStyle_Orange))
    }

    private fun showTimeDialog(initValue: Int, action: (Int) -> Unit) {
        MaterialDialog(context!!).show {
            timePicker(currentTime = LocalTime(initValue.toLong()).toDateTimeToday().toGregorianCalendar()) { _, datetime ->
                action.invoke(DateTime(datetime).withZone(DateTimeZone.UTC).millisOfDay)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_accept) saveTheme()
        return true
    }

    private fun saveTheme() {
        prefs().putInt(ThemeExtras.THEME_KEY, themePreference.getTheme())
        prefs().putInt(ThemeExtras.ASCENT_KEY, ascentPreference.getAscentStyle())
        prefs().putInt(ThemeExtras.NIGHT_THEME_KEY, themePreference.getNightTheme())
        prefs().putInt(ThemeExtras.NIGHT_THEME_START_KEY, themePreference.getNightModeStartTime())
        prefs().putInt(ThemeExtras.NIGHT_THEME_END_KEY, themePreference.getNightModeEndTime())
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