package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.toUri

class SettingsFragment : BaseSettingsFragment() {

    override val preferenceScreen: Int
        get() = R.xml.preferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(R.string.settings_about_send_key)?.apply {
            setOnPreferenceClickListener {
                val sendMail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gnoemes@gmail.com", null))
                sendMail.putExtra(Intent.EXTRA_EMAIL, arrayOf("gnoemes@gmail.com"))
                startActivity(Intent.createChooser(sendMail, null))
                return@setOnPreferenceClickListener true
            }
        }

        preference(R.string.settings_roadmap_key)?.apply {
            setOnPreferenceClickListener {
                openWeb(Constants.ROAD_MAP_URL)
                return@setOnPreferenceClickListener true
            }
        }

        preference(R.string.settings_about_forum_key)?.apply {
            setOnPreferenceClickListener {
                openWeb(Constants.FOUR_PDA_THEME_URL)
                return@setOnPreferenceClickListener true
            }
        }

        preference(R.string.settings_club_key)?.apply {
            setOnPreferenceClickListener {
                openWeb(Constants.APP_CLUB_URL)
                return@setOnPreferenceClickListener true
            }
        }
    }

    private fun openWeb(url : String) {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}