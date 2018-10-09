package com.gnoemes.shikimori.entity.anime.domain

import com.google.gson.annotations.SerializedName

enum class AnimeVideoType {
    @SerializedName("op")
    OPENING,
    @SerializedName("ed")
    ENDING,
    @SerializedName("pv")
    PROMO,
    @SerializedName("other")
    OTHER;
}