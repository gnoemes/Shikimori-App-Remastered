package com.gnoemes.shikimori.presentation.view.settings

import android.os.Bundle
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.activity.BaseThemedActivity
import com.gnoemes.shikimori.presentation.view.settings.fragments.SettingsFragment
import com.gnoemes.shikimori.utils.addBackButton
import kotlinx.android.synthetic.main.layout_toolbar.*

class SettingsActivity : BaseThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar?.setTitle(R.string.more_settings)
        toolbar?.addBackButton { super.onBackPressed() }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
    }
}