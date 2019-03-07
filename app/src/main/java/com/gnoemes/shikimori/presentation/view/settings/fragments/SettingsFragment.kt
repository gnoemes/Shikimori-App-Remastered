package com.gnoemes.shikimori.presentation.view.settings.fragments

import com.gnoemes.shikimori.R

class SettingsFragment : BaseSettingsFragment() {

    override val preferenceScreen: Int
        get() = R.xml.preferences
}