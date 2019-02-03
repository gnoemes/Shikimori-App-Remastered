package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import com.gnoemes.shikimori.R

class SettingsThemeFragment : BaseSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        //TODO add themes
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_theme
}