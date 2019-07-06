package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class StatResponse(
    @field:SerializedName("anime") val anime : List<StatisticResponse>,
    @field:SerializedName("manga") val manga : List<StatisticResponse>?
)