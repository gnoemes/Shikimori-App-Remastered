package com.gnoemes.shikimori.presentation.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.activity.BaseThemedActivity
import com.gnoemes.shikimori.presentation.view.settings.fragments.SettingsFragment
import com.gnoemes.shikimori.utils.addBackButton
import kotlinx.android.synthetic.main.layout_toolbar.*

class SettingsActivity : BaseThemedActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar?.setTitle(R.string.more_settings)
        toolbar?.addBackButton { super.onBackPressed() }

        replaceFragment(SettingsFragment())
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference?): Boolean {

        val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                pref?.fragment!!,
                pref.extras)
                .apply { setTargetFragment(caller, 0) }

        toolbar.title = "$pref"
        replaceFragment(fragment)

        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

}