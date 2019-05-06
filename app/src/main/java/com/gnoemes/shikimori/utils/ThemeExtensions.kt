package com.gnoemes.shikimori.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import org.joda.time.LocalTime

var Context.getCurrentTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.THEME_KEY, R.style.ShikimoriAppTheme_Default)
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.THEME_KEY, value)

var Context.getCurrentNightTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.NIGHT_THEME_KEY, Constants.NO_ID.toInt())
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.NIGHT_THEME_KEY, value)

var Context.getCurrentAscentTheme: Int
    get() = getThemeSharedPreferences().getInt(ThemeExtras.ASCENT_KEY, R.style.AscentStyle_Orange)
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.ASCENT_KEY, value)

var Context.getNightThemeStartTime: LocalTime
    get() = LocalTime(getThemeSharedPreferences().getInt(ThemeExtras.NIGHT_THEME_START_KEY, LocalTime(20, 0, 0, 0).millisOfDay).toLong())
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.NIGHT_THEME_START_KEY, value.millisOfDay)

var Context.getNightThemeEndTime: LocalTime
    get() = LocalTime(getThemeSharedPreferences().getInt(ThemeExtras.NIGHT_THEME_END_KEY, LocalTime(8, 0, 0, 0).millisOfDay).toLong())
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.NIGHT_THEME_END_KEY, value.millisOfDay)

val Context.isNightTime: Boolean
    get() {
        val now = LocalTime.now()
        val startTime = getNightThemeStartTime
        val endTime = getNightThemeEndTime
        return if (startTime.isBefore(endTime)) startTime.isBeforeOrEqual(now) && now.isBeforeOrEqual(endTime)
        else startTime.isBeforeOrEqual(now) && now.isBeforeOrEqual(LocalTime(23, 59, 59, 999)) || LocalTime.MIDNIGHT.isBeforeOrEqual(now) && now.isBeforeOrEqual(endTime)
    }

fun Context.getThemeSharedPreferences(): SharedPreferences {
    return getSharedPreferences(packageName + "_theme" + "preferences", Context.MODE_PRIVATE)
}

fun Activity.setTheme() {
    val nightTheme = getCurrentNightTheme
    when {
        nightTheme == Constants.NO_ID.toInt() -> setTheme(getCurrentTheme)
        isNightTime -> setTheme(nightTheme)
        else -> setTheme(getCurrentTheme)
    }
    theme.applyStyle(getCurrentAscentTheme, true)
}

fun Context.wrapTheme(@StyleRes style: Int): ContextThemeWrapper {
    val baseWrap = ContextThemeWrapper(this, style)
    val nightTheme = getCurrentNightTheme
    val wrapStyle = when {
        nightTheme == Constants.NO_ID.toInt() -> getCurrentTheme
        isNightTime -> nightTheme
        else -> getCurrentTheme
    }
    return ContextThemeWrapper(baseWrap, wrapStyle)
}