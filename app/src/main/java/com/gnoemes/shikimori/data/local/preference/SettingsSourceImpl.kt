package com.gnoemes.shikimori.data.local.preference

import android.content.SharedPreferences
import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.utils.putBoolean
import com.gnoemes.shikimori.utils.putString
import javax.inject.Inject

class SettingsSourceImpl @Inject constructor(
        @SettingsQualifier private val prefs: SharedPreferences
) : SettingsSource {

    override var isAutoStatus: Boolean
        get() = prefs.getBoolean(SettingsExtras.IS_AUTO_STATUS, true)
        set(value) = prefs.putBoolean(SettingsExtras.IS_AUTO_STATUS, value)

    override var isRomadziNaming: Boolean
        get() = prefs.getBoolean(SettingsExtras.IS_ROMADZI_NAMING, false)
        set(value) = prefs.putBoolean(SettingsExtras.IS_ROMADZI_NAMING, value)

    override var isRememberTranslationType: Boolean
        get() = prefs.getBoolean(SettingsExtras.IS_REMEMBER_TRANSLATION_TYPE, false)
        set(value) = prefs.putBoolean(SettingsExtras.IS_REMEMBER_TRANSLATION_TYPE, value)

    override var isRememberPlayer: Boolean
        get() = prefs.getBoolean(SettingsExtras.IS_REMEMBER_PLAYER, false)
        set(value) = prefs.putBoolean(SettingsExtras.IS_REMEMBER_PLAYER, value)

    override var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(SettingsExtras.IS_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.putBoolean(SettingsExtras.IS_NOTIFICATIONS_ENABLED, value)

    override var translationType: TranslationType
        get() {
            val type = prefs.getString(SettingsExtras.TRANSLATION_TYPE, "")
            return TranslationType.values().find { it.isEqualType(type) } ?: TranslationType.ALL
        }
        set(value) = prefs.putString(SettingsExtras.TRANSLATION_TYPE, value.type)

    override var playerType: PlayerType
        get() {
            val type = prefs.getString(SettingsExtras.PLAYER_TYPE, PlayerType.EMBEDDED.name)!!
            return PlayerType.valueOf(type)
        }
        set(value) = prefs.putString(SettingsExtras.PLAYER_TYPE, value.name)




}