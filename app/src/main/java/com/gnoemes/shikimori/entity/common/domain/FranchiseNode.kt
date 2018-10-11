package com.gnoemes.shikimori.entity.common.domain

import org.joda.time.DateTime

data class FranchiseNode(
        val id: Long,
        val date: DateTime,
        val name: String,
        val imageUrl: String?,
        val url: String,
        val year: Int?,
        val type: String?,
        val weight: Int
)