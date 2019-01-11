package com.gnoemes.shikimori.entity.series.domain

import com.google.gson.annotations.SerializedName

enum class VideoHosting(
        val type: String,
        val synonymType: String
) {
    @SerializedName("sibnet.ru")
    SIBNET("sibnet", "sibnet.ru"),
    @SerializedName("smotretanime.ru")
    SMOTRET_ANIME("smotretanime", "smotretanime.ru"),
    @SerializedName("vk.com")
    VK("vk", "vk.com"),
    @SerializedName("ok.ru")
    OK("ok", "ok.ru"),
    @SerializedName("mail.ru")
    MAIL_RU("mailru", "mail.ru"),
    @SerializedName("sovetromantica.com")
    SOVET_ROMANTICA("sovetromantica", "sovetromantica.com"),
    @SerializedName("myvi.ru")
    MY_VI("myvi", "myvi.ru"),
    @SerializedName("animedia.tv")
    ANIMEDIA("animedia", "animedia.tv"),
    @SerializedName("rutube.ru")
    RUTUBE("rutube", "rutube.ru"),
    @SerializedName("youtube.com")
    YOUTUBE("youtube", "youtube.com"),
    @SerializedName("")
    UNKNOWN("", "");

    fun isEqualType(otherType: String): Boolean {
        return this.type == otherType || this.synonymType == otherType
    }
}