package com.gnoemes.shikimori.entity.app

import com.google.gson.annotations.SerializedName

data class TokenResponse(
        @field:SerializedName("access_token") val accessToken: String,
        @field:SerializedName("refresh_token") val refreshToken: String
)