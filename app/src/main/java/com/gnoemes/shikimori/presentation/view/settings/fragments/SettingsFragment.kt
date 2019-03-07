package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.presentation.SettingsScreens
import com.gnoemes.shikimori.presentation.view.settings.SettingsNavigator
import com.gnoemes.shikimori.utils.preference

//app:fragment navigation causes ClassNotFoundException sometimes
class SettingsFragment : BaseSettingsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference(SettingsScreens.GENERAL)?.onPreferenceClickListener = clickListener
        preference(SettingsScreens.NOTIFICATIONS)?.onPreferenceClickListener = clickListener
        preference(SettingsScreens.PLAYER)?.onPreferenceClickListener = clickListener
        preference(SettingsScreens.THEME)?.onPreferenceClickListener = clickListener
        preference(SettingsScreens.ABOUT)?.onPreferenceClickListener = clickListener
    }

    private val clickListener = Preference.OnPreferenceClickListener {
        (activity as? SettingsNavigator)?.navigateTo(it)
        return@OnPreferenceClickListener true
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences
}