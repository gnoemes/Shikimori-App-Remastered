package com.gnoemes.shikimori.entity.club.domain

import com.google.gson.annotations.SerializedName

enum class ClubPolicy {
    @SerializedName("free")
    FREE,
    @SerializedName("admin_invite")
    ADMIN_INVITE,
    @SerializedName("owner_invite")
    OWNER_INVITE
}