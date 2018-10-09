package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class UserUnreadMessagesCount(
        @field:SerializedName("messages") val messages : Int,
        @field:SerializedName("news") val news : Int,
        @field:SerializedName("notifications") val notifications : Int
)