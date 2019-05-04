package com.gnoemes.shikimori.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras

var Context.getCurrentTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default)
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.THEME_KEY, value)

var Context.getCurrentAscentTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.ASCENT_KEY, R.style.AscentStyle_Orange)
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.ASCENT_KEY, value)


fun Context.getThemeSharedPreferences(): SharedPreferences {
    return getSharedPreferences(packageName + "_theme" + "preferences", Context.MODE_PRIVATE)
}

fun Activity.setTheme() {
    setTheme(getCurrentTheme)
    theme.applyStyle(getCurrentAscentTheme, true)
}