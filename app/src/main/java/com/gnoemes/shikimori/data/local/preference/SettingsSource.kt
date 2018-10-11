package com.gnoemes.shikimori.data.local.preference

import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType

interface SettingsSource {
    fun getAutoStatus(): Boolean

    fun setAutoStatus(status: Boolean)

    fun getRomadziNaming(): Boolean

    fun setRomadziNaming(value: Boolean)

    fun setRememberType(value: Boolean)

    fun isRememberType(): Boolean

    fun setRememberPlayer(value: Boolean)

    fun IsRememberPlayer(): Boolean

    fun setType(value: TranslationType)

    fun getType(): TranslationType

    fun setPlayer(value: PlayerType)

    fun getPlayer(): PlayerType

    fun getDownloadLocation(): Int

    fun getNotificationsEnabled(): Boolean
}