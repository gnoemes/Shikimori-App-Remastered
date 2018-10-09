package com.gnoemes.shikimori.entity.manga.domain

import com.google.gson.annotations.SerializedName

enum class MangaType(val type: String) {
    @SerializedName("manga")
    MANGA("manga"),
    @SerializedName("manhwa")
    MANHWA("manhwa"),
    @SerializedName("manhua")
    MANHUA("manhua"),
    @SerializedName("novel")
    NOVEL("novel"),
    @SerializedName("one_shot")
    ONE_SHOT("one_shot"),
    @SerializedName("doujin")
    DOUJIN("doujin"),
    @SerializedName("")
    UNKNOWN("");
}