package com.gnoemes.shikimori.presentation.view.base.activity

import android.os.Bundle
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.aesthetic.AutoSwitchMode
import com.afollestad.aesthetic.BottomNavBgMode
import com.afollestad.aesthetic.BottomNavIconTextMode
import com.gnoemes.shikimori.R

abstract class BaseThemedActivity  : MvpActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Aesthetic.attach(this)
        super.onCreate(savedInstanceState)
        configureAesthetic()
    }

    override fun onPause() {
        Aesthetic.pause(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Aesthetic.resume(this)
    }

    private fun configureAesthetic() {
        if (Aesthetic.isFirstTime) {
            Aesthetic.config {
                activityTheme(R.style.ShikimoriAppTheme_Default)
                colorPrimary(res = R.color.default_colorPrimary)
                colorPrimaryDark(res = R.color.default_colorPrimaryDark)
                colorAccent(res = R.color.default_colorAccent)
                colorWindowBackground(res = R.color.default_colorBackground)
                colorStatusBarAuto()
                colorNavigationBarAuto()
                lightStatusBarMode(AutoSwitchMode.AUTO)
                bottomNavigationBackgroundMode(BottomNavBgMode.PRIMARY)
                bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                toolbarTitleColor(res = R.color.default_colorOnPrimary)
                toolbarIconColor(R.color.default_colorOnPrimary)
                textColorPrimary(res = R.color.default_colorOnSurface)
                textColorSecondary(res = R.color.default_colorOnSurface)
                textColorPrimaryInverse(res = R.color.default_colorOnAccent)
                textColorSecondaryInverse(res = R.color.default_colorOnAccent)
                colorCardViewBackground(res = R.color.default_colorSurface)
            }
        }
    }
}