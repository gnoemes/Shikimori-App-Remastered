package com.gnoemes.shikimori.entity.roles.data

import com.google.gson.annotations.SerializedName

data class BirthdayResponse (
        @field:SerializedName("day") val day : String?,
        @field:SerializedName("year") val year : String?,
        @field:SerializedName("month") val month : String?
)