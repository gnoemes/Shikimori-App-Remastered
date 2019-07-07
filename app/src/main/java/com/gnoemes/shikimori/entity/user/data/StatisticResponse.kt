package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class StatisticResponse(
        @field:SerializedName("name") val name : String,
        @field:SerializedName("value") val value : Int
)