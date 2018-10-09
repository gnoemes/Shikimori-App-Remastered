package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class FullStatusResponse(
        @field:SerializedName("anime") val anime : List<StatusResponse>,
        @field:SerializedName("manga") val manga : List<StatusResponse>
)