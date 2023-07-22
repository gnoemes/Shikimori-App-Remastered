package com.gnoemes.shikimori.entity.series.data.ok

import kotlinx.serialization.Serializable

@Serializable
data class OkPlayerDataJsonModel(
        val flashvars: OkPlayerDataFlashvars
)
