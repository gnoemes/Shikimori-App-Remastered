package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.colorAttr

abstract class BaseSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(preferenceScreen, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView?.background = ColorDrawable(context?.colorAttr(R.attr.colorSurface)!!)
    }

    abstract val preferenceScreen : Int
}