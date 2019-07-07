package com.gnoemes.shikimori.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AscentTheme
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.Theme
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import org.joda.time.Duration
import org.joda.time.LocalTime

var Context.getCurrentTheme: Int
    get() {
        val themeIndex = getThemeSharedPreferences().getInt(ThemeExtras.THEME_KEY, Theme.DEFAULT.index)
        return when (Theme.values().find { it.index == themeIndex } ?: Theme.DEFAULT) {
            Theme.DEFAULT -> R.style.ShikimoriAppTheme_Default
            Theme.DARK -> R.style.ShikimoriAppTheme_Dark
            Theme.AMOLED -> R.style.ShikimoriAppTheme_Amoled
        }
    }
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.THEME_KEY, value)

var Context.getCurrentNightTheme: Int
    get() {
        val themeIndex  = getThemeSharedPreferences().getInt(ThemeExtras.NIGHT_THEME_KEY, Constants.NO_ID.toInt())
        return when(Theme.values().find { it.index == themeIndex } ?: return Constants.NO_ID.toInt()) {
            Theme.DEFAULT -> R.style.ShikimoriAppTheme_Default
            Theme.DARK -> R.style.ShikimoriAppTheme_Dark
            Theme.AMOLED -> R.style.ShikimoriAppTheme_Amoled
        }
    }
    set(value) = getThemeSharedPreferences().putInt(ThemeExtras.NIGHT_THEME_KEY, value)

var Context.getCurrentAscentTheme: Int
    get() {
        val themeIndex = getThemeSharedPreferences().getInt(ThemeExtras.ASCENT_KEY, AscentTheme.ORANGE.index)
        return when (AscentTheme.values().find { it.index == themeIndex } ?: AscentTheme.ORANGE) {
            AscentTheme.RED -> R.style.AscentStyle_Red
            AscentTheme.ORANGE -> R.style.AscentStyle_Orange
            AscentTheme.YELLOW -> R.style.AscentStyle_Yellow
            AscentTheme.GREEN -> R.style.AscentStyle_Green
            AscentTheme.CYAN -> R.style.AscentStyle_Cyan
            AscentTheme.BLUE -> R.style.AscentStyle_Blue
            AscentTheme.PURPLE -> R.style.AscentStyle_Purple
        }
    }
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

fun Activity.setTheme(recreationRunnable: Runnable? = null) {
    val nightTheme = getCurrentNightTheme
    val nightThemeExist = nightTheme != Constants.NO_ID.toInt()
    when {
        !nightThemeExist -> setTheme(getCurrentTheme)
        isNightTime -> setTheme(nightTheme)
        else -> setTheme(getCurrentTheme)

    }

    if (nightThemeExist) scheduleNightTheme(recreationRunnable)

    theme.applyStyle(getCurrentAscentTheme, true)
}

val themeHandler: Handler by lazy { Handler() }

fun Activity.scheduleNightTheme(runnable: Runnable?) {
    if (runnable == null) return
    val now = LocalTime.now()
    val startTime = getNightThemeStartTime
    val endTime = getNightThemeEndTime
    val lastDayTime = LocalTime(23, 59, 59, 999)
    val delayMills =
            if (isNightTime) {
                if (endTime.isBefore(now)) lastDayTime.millisOfDay - now.millisOfDay + endTime.millisOfDay
                else endTime.millisOfDay - now.millisOfDay
            } else {
                if (startTime.isBefore(now)) lastDayTime.millisOfDay - now.millisOfDay + startTime.millisOfDay
                else startTime.millisOfDay - now.millisOfDay
            }
    Log.i("NIGHT_THEME_KEY", "mills: $delayMills human: ${Duration.millis(delayMills.toLong())}")
    themeHandler.postDelayed(runnable, delayMills.toLong())
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