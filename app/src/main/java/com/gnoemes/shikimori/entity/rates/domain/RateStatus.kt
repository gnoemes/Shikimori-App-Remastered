package com.gnoemes.shikimori.entity.rates.domain

import com.google.gson.annotations.SerializedName

enum class RateStatus(val status : String) {
    @SerializedName("watching")
    WATCHING("watching"),
    @SerializedName("planned")
    PLANNED("planned"),
    @SerializedName("rewatching")
    REWATCHING("rewatching"),
    @SerializedName("completed")
    COMPLETED("completed"),
    @SerializedName("on_hold")
    ON_HOLD("on_hold"),
    @SerializedName("dropped")
    DROPPED("dropped");
}