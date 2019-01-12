package com.gnoemes.shikimori.entity.comment.domain

import com.google.gson.annotations.SerializedName

enum class CommentableType(val type : String) {
    @SerializedName("Topic")
    TOPIC("Topic"),
    @SerializedName("User")
    USER("User")
}