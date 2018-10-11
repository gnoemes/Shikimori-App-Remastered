package com.gnoemes.shikimori.entity.common.domain

abstract class LinkedContent(
        val linkedId: Long,
        val linkedType: Type,
        val imageUrl: String?,
        val linkedName: String
)