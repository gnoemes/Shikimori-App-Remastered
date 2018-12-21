package com.gnoemes.shikimori.entity.common.presentation.shikimori

import com.gnoemes.shikimori.entity.common.domain.Type
import com.google.gson.annotations.SerializedName

data class Link(
        @SerializedName("type") val type: Type,
        @SerializedName("id") val id: Long,
        @SerializedName("text") val text: String
) : Content(ContentType.LINK)