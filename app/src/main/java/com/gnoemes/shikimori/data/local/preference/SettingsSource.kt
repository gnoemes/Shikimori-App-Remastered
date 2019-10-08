package com.gnoemes.shikimori.data.local.preference

import com.gnoemes.shikimori.entity.chronology.ChronologyType
import com.gnoemes.shikimori.entity.rates.domain.RateSwipeAction
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType

interface SettingsSource {

    var isAutoStatus : Boolean

    var isAutoIncrement : Boolean

    var isRussianNaming: Boolean

    var isAskForPlayer : Boolean

    var isNotificationsEnabled : Boolean

    var translationType : TranslationType

    var playerType : PlayerType

    var useLocalTranslationSettings : Boolean

    var downloadFolder : String

    var isExternalBestQuality : Boolean

    var rateSwipeToLeftAction : RateSwipeAction

    var rateSwipeToRightAction : RateSwipeAction

    var chronologyType : ChronologyType
}