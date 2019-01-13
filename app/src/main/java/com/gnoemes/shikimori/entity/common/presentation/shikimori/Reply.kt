package com.gnoemes.shikimori.entity.common.presentation.shikimori

import com.gnoemes.shikimori.entity.common.domain.Type
import com.google.gson.annotations.SerializedName

data class Reply(
        @SerializedName("type") val type: Type,
        @SerializedName("ids") val ids: String
) : Content(ContentType.REPLY)