package com.gnoemes.shikimori.presentation.view.settings

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.presentation.SettingsScreens
import com.gnoemes.shikimori.presentation.view.base.activity.MvpActivity
import com.gnoemes.shikimori.presentation.view.settings.fragments.*
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.getCurrentTheme
import kotlinx.android.synthetic.main.layout_toolbar.*

class SettingsActivity : MvpActivity(), SettingsNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getCurrentTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar?.apply {
            setDefaultTitle()
            addBackButton { onBackPressed() }
        }

        replaceFragment(SettingsFragment())
    }

    override fun navigateTo(pref: Preference) {
        toolbar.title = "$pref"

        when (pref.key) {
            SettingsScreens.GENERAL -> replaceFragment(SettingsGeneralFragment())
            SettingsScreens.ABOUT -> replaceFragment(SettingsAboutFragment())
            SettingsScreens.PLAYER -> replaceFragment(SettingsPlayerFragment())
            SettingsScreens.THEME -> replaceFragment(SettingsThemeFragment())
            //TODO notifications
            SettingsScreens.NOTIFICATIONS -> Unit
            else -> throw ClassNotFoundException()
        }
    }

    override fun onBackPressed() {
        toolbar?.setDefaultTitle()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is SettingsFragment) super.finish()
        else super.onBackPressed()
    }

    private fun Toolbar.setDefaultTitle() = setTitle(R.string.more_settings)

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment::class.toString())
                .commit()
    }
}