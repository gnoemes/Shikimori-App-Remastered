package com.gnoemes.shikimori.presentation.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras.NEW_VERSION_AVAILABLE
import com.gnoemes.shikimori.presentation.view.base.activity.MvpActivity
import com.gnoemes.shikimori.presentation.view.settings.fragments.SettingsFragment
import com.gnoemes.shikimori.utils.*
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.layout_toolbar.*

class SettingsActivity : MvpActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, ToolbarCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar?.apply {
            setDefaultTitle()
            addBackButton { onBackPressed() }
            inflateMenu(R.menu.menu_setting)
            menu.findItem(R.id.item_accept).isVisible = false
            setOnMenuItemClickListener {
                (supportFragmentManager.findFragmentById(R.id.fragment_container) as? Toolbar.OnMenuItemClickListener)?.onMenuItemClick(it)
                        ?: false
            }
        }

        val versionView = View.inflate(this, R.layout.item_setting_menu, null) as TextView
        versionView.text = BuildConfig.VERSION_NAME.replace(Regex("[^0-9.]"), "")


        toolbar.menu.findItem(R.id.item_version).actionView = versionView
        toolbar.layoutParams = (toolbar.layoutParams as AppBarLayout.LayoutParams).apply {
            scrollFlags = 0
        }

        if (getDefaultSharedPreferences().getBoolean(NEW_VERSION_AVAILABLE, false)) onNewVersionAvailable()

        replaceFragment(SettingsFragment())
    }

    override fun onBackPressed() {
        toolbar?.setDefaultTitle()
        showVersion(true)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is SettingsFragment) super.finish()
        else super.onBackPressed()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference?): Boolean {

        val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                pref?.fragment!!,
                pref.extras)
                .apply { setTargetFragment(caller, 0) }

        toolbar?.title = pref.title
        showVersion(false)
        replaceFragment(fragment)

        return true
    }

    private fun Toolbar.setDefaultTitle() = setTitle(R.string.more_settings)

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment::class.toString())
                .commit()
    }

    private fun showVersion(show: Boolean) {
        toolbar.menu.findItem(R.id.item_version).isVisible = show
    }

    private fun onNewVersionAvailable() {
        (toolbar.menu.findItem(R.id.item_version).actionView as TextView).apply {
            val indicator = context.drawable(R.drawable.ic_version_indicator)?.apply { tint(context.colorAttr(R.attr.colorSecondary)) }
            setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null)
            onClick { startActivity(Intent(Intent.ACTION_VIEW, Constants.FOUR_PDA_THEME_URL.toUri())) }
        }
    }

    override fun showToolbarMenu() = run { toolbar.menu.findItem(R.id.item_accept).isVisible = true }
    override fun hideToolbarMenu() = run { toolbar.menu.findItem(R.id.item_accept).isVisible = false }
}