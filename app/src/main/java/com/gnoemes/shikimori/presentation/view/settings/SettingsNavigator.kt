package com.gnoemes.shikimori.presentation.view.settings

import androidx.preference.Preference

interface SettingsNavigator {
    fun navigateTo(pref : Preference)
}