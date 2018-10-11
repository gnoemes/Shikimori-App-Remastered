package com.gnoemes.shikimori.entity.forum.domain

data class Forum(
        val id: Long,
        val name: String,
        val type: ForumType,
        val url: String
)