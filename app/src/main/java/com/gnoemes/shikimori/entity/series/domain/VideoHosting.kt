package com.gnoemes.shikimori.entity.series.domain

import com.google.gson.annotations.SerializedName

enum class VideoHosting(
        val type: String,
        val synonymType: String
) {
    @SerializedName("sibnet.ru", alternate = ["sibnet"])
    SIBNET("sibnet", "sibnet.ru"),
    @SerializedName("smotretanime.ru", alternate = ["smotretanime"])
    SMOTRET_ANIME("smotretanime", "smotretanime.ru"),
    @SerializedName("vk.com", alternate = ["vk"])
    VK("vk", "vk.com"),
    @SerializedName("ok.ru", alternate = ["ok"])
    OK("ok", "ok.ru"),
    @SerializedName("mail.ru", alternate = ["mail"])
    MAIL_RU("mailru", "mail.ru"),
    @SerializedName("sovetromantica.com", alternate = ["sovetromantica"])
    SOVET_ROMANTICA("sovetromantica", "sovetromantica.com"),
    @SerializedName("myvi.top", alternate = ["myvi"])
    MY_VI("myvi", "myvi.top"),
    @SerializedName("animedia.tv", alternate = ["animedia"])
    ANIMEDIA("animedia", "animedia.tv"),
    @SerializedName("rutube.ru", alternate = ["rutube"])
    RUTUBE("rutube", "rutube.ru"),
    @SerializedName("youtube.com", alternate = ["youtube"])
    YOUTUBE("youtube", "youtube.com"),
    @SerializedName("")
    UNKNOWN("unknown", "unknown");

    fun isEqualType(otherType: String): Boolean {
        return this.type == otherType || this.synonymType == otherType
    }
}