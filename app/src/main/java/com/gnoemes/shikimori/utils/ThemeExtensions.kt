package com.gnoemes.shikimori.utils

import android.content.Context
import android.content.SharedPreferences
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras

var Context.getCurrentTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default)
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.THEME_KEY, value)

fun Context.getThemeSharedPreferences(): SharedPreferences {
    return getSharedPreferences(packageName + "_theme" + "preferences", Context.MODE_PRIVATE)
}