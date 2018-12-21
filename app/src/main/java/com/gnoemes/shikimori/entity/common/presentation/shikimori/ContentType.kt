package com.gnoemes.shikimori.entity.common.presentation.shikimori

import com.google.gson.annotations.SerializedName

enum class ContentType {
    TEXT,
    @SerializedName("link")
    LINK,

}