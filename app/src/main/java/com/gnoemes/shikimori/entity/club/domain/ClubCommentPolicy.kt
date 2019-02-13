package com.gnoemes.shikimori.entity.club.domain

import com.google.gson.annotations.SerializedName

enum class ClubCommentPolicy {
    @SerializedName("free")
    FREE,
    @SerializedName("members")
    MEMBERS,
}