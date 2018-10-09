package com.gnoemes.shikimori.entity.rates.data

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class UserRateResponse(
        @field:SerializedName("id") val id: Long? = null,
        @field:SerializedName("user_id") val userId: Long? = null,
        @field:SerializedName("target_id") val targetId: Long? = null,
        @field:SerializedName("target_type") val targetType: Type? = null,
        @field:SerializedName("score") val score: Double? = null,
        @field:SerializedName("status") val status: RateStatus? = null,
        @field:SerializedName("rewatches") val rewatches: Int? = null,
        @field:SerializedName("episodes") val episodes: Int? = null,
        @field:SerializedName("volumes") val volumes: Int? = null,
        @field:SerializedName("chapters") val chapters: Int? = null,
        @field:SerializedName("text") val text: String? = null,
        @field:SerializedName("text_html") val textHtml: String? = null,
        @field:SerializedName("created_at") val dateCreated: DateTime? = null,
        @field:SerializedName("updated_at") val dateUpdated: DateTime? = null
)