package com.gnoemes.shikimori.entity.user.domain

import com.google.gson.annotations.SerializedName

enum class MessageType {
    @SerializedName("inbox")
    INBOX,
    @SerializedName("private")
    PRIVATE,
    @SerializedName("sent")
    SENT,
    @SerializedName("news")
    NEWS,
    @SerializedName("notifications")
    NOTIFICATIONS,
    @SerializedName("episode")
    EPISODE,
    @SerializedName("released")
    RELEASED,
    @SerializedName("anons")
    ANONS,
    @SerializedName("ongoing")
    ONGOING,
}