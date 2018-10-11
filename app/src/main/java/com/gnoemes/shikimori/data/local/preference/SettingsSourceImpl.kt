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

    override fun getAutoStatus(): Boolean = prefs.getBoolean(SettingsExtras.IS_AUTO_STATUS, true)

    override fun setAutoStatus(status: Boolean) {
        prefs.putBoolean(SettingsExtras.IS_AUTO_STATUS, status)
    }

    override fun getRomadziNaming(): Boolean = prefs.getBoolean(SettingsExtras.IS_ROMADZI_NAMING, false)

    override fun setRomadziNaming(value: Boolean) {
        prefs.putBoolean(SettingsExtras.IS_ROMADZI_NAMING, value)
    }

    override fun setRememberType(value: Boolean) {
        prefs.putBoolean(SettingsExtras.IS_REMEMBER_TRANSLATION_TYPE, value)
    }

    override fun isRememberType(): Boolean = prefs.getBoolean(SettingsExtras.IS_REMEMBER_TRANSLATION_TYPE, false)

    override fun setRememberPlayer(value: Boolean) {
        prefs.putBoolean(SettingsExtras.IS_REMEMBER_PLAYER, value)
    }

    override fun IsRememberPlayer(): Boolean = prefs.getBoolean(SettingsExtras.IS_REMEMBER_PLAYER, false)

    override fun setType(value: TranslationType) {
        prefs.putString(SettingsExtras.TRANSLATION_TYPE, value.type)
    }

    override fun getType(): TranslationType {
        val type = prefs.getString(SettingsExtras.TRANSLATION_TYPE, "")
        return TranslationType.values().find { it.isEqualType(type) } ?: TranslationType.ALL
    }

    override fun setPlayer(value: PlayerType) {
        prefs.putString(SettingsExtras.PLAYER_TYPE, value.name)
    }

    override fun getPlayer(): PlayerType {
        val type = prefs.getString(SettingsExtras.PLAYER_TYPE, PlayerType.EMBEDDED.name)!!
        return PlayerType.valueOf(type)
    }

    override fun getDownloadLocation(): Int = prefs.getInt(SettingsExtras.DOWNLOAD_LOCATION_TYPE, 1)

    override fun getNotificationsEnabled(): Boolean = prefs.getBoolean(SettingsExtras.IS_NOTIFICATIONS_ENABLED, true)

}