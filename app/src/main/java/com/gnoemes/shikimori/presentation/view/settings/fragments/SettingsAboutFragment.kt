package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.Intent
import android.net.Uri
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

        preference(R.string.settings_about_send_key)?.apply {
            setOnPreferenceClickListener {
                val sendMail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gnoemes@gmail.com", null))
                sendMail.putExtra(Intent.EXTRA_EMAIL, arrayOf("gnoemes@gmail.com"))
                startActivity(Intent.createChooser(sendMail, null))
                return@setOnPreferenceClickListener true
            }
        }
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_about
}