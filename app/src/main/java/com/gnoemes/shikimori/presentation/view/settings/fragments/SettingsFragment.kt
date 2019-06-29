package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.presentation.view.common.widget.preferences.AppGroupPreference
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.prefs
import com.gnoemes.shikimori.utils.toUri

class SettingsFragment : BaseSettingsFragment() {

    override val preferenceScreen: Int
        get() = R.xml.preferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        (preference("info_group") as? AppGroupPreference)?.apply {
            mailClickListener = View.OnClickListener {
                val sendMail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gnoemes@gmail.com", null))
                sendMail.putExtra(Intent.EXTRA_EMAIL, arrayOf("gnoemes@gmail.com"))
                startActivity(Intent.createChooser(sendMail, null))
            }
            trelloClickListener = View.OnClickListener { openWeb(Constants.ROAD_MAP_URL) }
            forumClickListener = View.OnClickListener { openWeb(Constants.FOUR_PDA_THEME_URL) }
            clubClickListener = View.OnClickListener { openWeb(Constants.APP_CLUB_URL) }
            donationClickListener = View.OnClickListener { openWeb(prefs().getString(SettingsExtras.DONATION_LINK, Constants.DEFAULT_DONATION_LINK)!!) }
        }
    }

    private fun openWeb(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}