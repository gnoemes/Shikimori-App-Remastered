package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.preference

class SettingsAboutFragment : BaseSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(R.string.settings_about_version_key)?.apply {
            summary = BuildConfig.VERSION_NAME
        }
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_about
}