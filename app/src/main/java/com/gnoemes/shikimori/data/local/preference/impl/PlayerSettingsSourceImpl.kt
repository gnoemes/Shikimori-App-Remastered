package com.gnoemes.shikimori.data.local.preference.impl

import android.content.SharedPreferences
import com.gnoemes.shikimori.data.local.preference.PlayerSettingsSource
import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.utils.putBoolean
import com.gnoemes.shikimori.utils.putLong
import javax.inject.Inject

class PlayerSettingsSourceImpl @Inject constructor(
        @SettingsQualifier private val prefs: SharedPreferences
) : PlayerSettingsSource {

    override var isGesturesEnabled: Boolean
        get() = prefs.getBoolean(SettingsExtras.PLAYER_IS_GESTURES_ENABLED, true)
        set(value) = prefs.putBoolean(SettingsExtras.PLAYER_IS_GESTURES_ENABLED, value)

    override var isVolumeAndBrightnessGesturesEnabled: Boolean
        get() = prefs.getBoolean(SettingsExtras.PLAYER_IS_VOLUME_BRIGHTNESS_GESTURES_ENABLED, true)
        set(value) = prefs.putBoolean(SettingsExtras.PLAYER_IS_VOLUME_BRIGHTNESS_GESTURES_ENABLED, value)

    override var isVolumeAndBrightnessInverted: Boolean
        get() = prefs.getBoolean(SettingsExtras.PLAYER_IS_VOLUME_AND_BRIGHTNESS_INVERTED, false)
        set(value) = prefs.putBoolean(SettingsExtras.PLAYER_IS_VOLUME_AND_BRIGHTNESS_INVERTED, value)

    override var isForwardRewindSlide: Boolean
        get() = prefs.getBoolean(SettingsExtras.PLAYER_IS_FORWARD_REWIND_SLIDE, false)
        set(value) = prefs.putBoolean(SettingsExtras.PLAYER_IS_FORWARD_REWIND_SLIDE, value)

    override var isOpenLandscape: Boolean
        get() = prefs.getBoolean(SettingsExtras.PLAYER_IS_OPEN_LANDSCAPE, true)
        set(value) = prefs.putBoolean(SettingsExtras.PLAYER_IS_OPEN_LANDSCAPE, value)

    override var forwardRewindOffset: Long
        get() = prefs.getLong(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET, 10000)
        set(value) = prefs.putLong(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET, value)

    override var forwardRewindOffsetBig: Long
        get() = prefs.getLong(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET_BIG, 90000)
        set(value) = prefs.putLong(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET_BIG, value)
}