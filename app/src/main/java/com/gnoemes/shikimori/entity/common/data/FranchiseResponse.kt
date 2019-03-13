package com.gnoemes.shikimori.entity.common.data

import com.google.gson.annotations.SerializedName

data class FranchiseResponse(
        @field:SerializedName("nodes") val nodes: List<FranchiseNodeResponse>
)