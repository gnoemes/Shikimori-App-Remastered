package com.gnoemes.shikimori.entity.series.data.ok

import kotlinx.serialization.Serializable

@Serializable
data class OkPlayerDataMetadata(
        val videos: List<OkPlayerDataVideo>
)
