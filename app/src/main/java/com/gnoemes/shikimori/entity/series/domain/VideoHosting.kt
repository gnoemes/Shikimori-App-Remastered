package com.gnoemes.shikimori.entity.series.domain

enum class VideoHosting(
        val type: String,
        val synonymType: String
) {
    SIBNET("sibnet", "sibnet.ru"),
    SMOTRET_ANIME("smotret-anime", "smotret-anime.ru"),
    VK("vk", "vk.com"),
    OK("ok", "ok.ru"),
    MAIL_RU("mailru", "mail.ru"),
    SOVET_ROMANTICA("sovetromantica", "sovetromantica.com"),
    MY_VI("myvi", "myvi.ru"),
    ANIMEDIA("animedia", "animedia.tv"),
    RUTUBE("rutube", "rutube.ru"),
    YOUTUBE("youtube", "youtube.com"),
    UNKNOWN("", "");

    fun isEqualType(otherType: String): Boolean {
        return this.type == otherType || this.synonymType == otherType
    }
}