package com.gnoemes.shikimori.entity.common.data

import com.gnoemes.shikimori.entity.common.domain.RelationType
import com.google.gson.annotations.SerializedName

data class FranchiseRelationResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("source_id") val sourceId: Long,
        @field:SerializedName("target_id") val targetId: Long,
        @field:SerializedName("source") val sourceNodeIndex: Int,
        @field:SerializedName("target") val targetNodeIndex: Int,
        @field:SerializedName("weight") val weight: Int,
        @field:SerializedName("relation") private val _relation: RelationType?
) {

    val relation: RelationType
        get() = _relation ?: RelationType.OTHER
}