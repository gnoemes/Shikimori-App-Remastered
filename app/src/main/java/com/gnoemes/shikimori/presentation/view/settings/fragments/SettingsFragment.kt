package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}