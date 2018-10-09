package com.gnoemes.shikimori.entity.common.domain

import com.google.gson.annotations.SerializedName

enum class Status(val status: String) {
    @SerializedName("anons")
    ANONS("anons"),
    @SerializedName("ongoing")
    ONGOING("ongoing"),
    @SerializedName("released")
    RELEASED("released"),
    NONE("none");

    fun equalsStatus(otherStatus: String): Boolean {
        return status == otherStatus
    }
}