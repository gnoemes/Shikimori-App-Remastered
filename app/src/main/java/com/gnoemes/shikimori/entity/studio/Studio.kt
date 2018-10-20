package com.gnoemes.shikimori.entity.studio

data class Studio(
        val id: Long,
        val name: String,
        val nameFiltered: String,
        val isReal: Boolean,
        val imageUrl: String?
)