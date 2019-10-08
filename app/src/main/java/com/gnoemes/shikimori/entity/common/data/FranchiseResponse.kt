package com.gnoemes.shikimori.entity.common.data

import com.google.gson.annotations.SerializedName

data class FranchiseResponse(
        @field:SerializedName("links") val relations : List<FranchiseRelationResponse>,
        @field:SerializedName("nodes") val nodes: List<FranchiseNodeResponse>
)