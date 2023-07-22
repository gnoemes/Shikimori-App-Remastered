package com.gnoemes.shikimori.entity.series.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class VideoHosting : Parcelable {
    abstract val type: String
    abstract val synonymType: String

    @Parcelize
    data class VK(
            override val type: String = "vk",
            override val synonymType: String = "vk.com"
    ) : VideoHosting()

    @Parcelize
    data class OK(
            override val type: String = "ok",
            override val synonymType: String = "ok.ru"
    ) : VideoHosting()

    @Parcelize
    data class KODIK(
            override val type: String = "aniqit",
            override val synonymType: String = "aniqit.com"
    ) : VideoHosting()

    @Parcelize
    data class SIBNET(
            override val type: String = "sibnet",
            override val synonymType: String = "sibnet.ru"
    ) : VideoHosting()

    @Parcelize
    data class SOVET_ROMANTICA(
            override val type: String = "sovetromantica",
            override val synonymType: String = "sovetromantica.com"
    ) : VideoHosting()

    @Parcelize
    data class SMOTRET_ANIME(
            override val type: String = "smotretanime",
            override val synonymType: String = "smotret-anime.online"
    ) : VideoHosting()

    @Parcelize
    data class UNKNOWN(
            override val type: String = "unknown",
            override val synonymType: String = "unknown"
    ) : VideoHosting()
}