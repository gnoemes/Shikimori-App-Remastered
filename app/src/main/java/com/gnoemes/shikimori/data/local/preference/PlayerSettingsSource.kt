package com.gnoemes.shikimori.data.local.preference

interface PlayerSettingsSource {
    var isGesturesEnabled : Boolean
    var isVolumeAndBrightnessGesturesEnabled : Boolean
    var isVolumeAndBrightnessInverted : Boolean
    var isForwardRewindSlide : Boolean
    var isOpenLandscape : Boolean
    var isZoomProportional : Boolean
    var isAutoPip : Boolean

    var forwardRewindOffset : Long
    var forwardRewindOffsetBig : Long
}