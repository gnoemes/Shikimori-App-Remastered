package com.gnoemes.shikimori.entity.user.data

import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class UserHistoryResponse(
        @field:SerializedName("id") val id : Long,
        @field:SerializedName("created_at") val dateCreated : DateTime,
        @field:SerializedName("description") val description : String,
        @field:SerializedName("target") val target : LinkedContentResponse?
)