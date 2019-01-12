package com.gnoemes.shikimori.entity.topic.domain

import com.google.gson.annotations.SerializedName

enum class TopicEvent(type: String) {
    @SerializedName("episode")
    EPISODE("episode"),
    @SerializedName("released")
    RELEASED("released"),
    @SerializedName("anons")
    NONE("")

}