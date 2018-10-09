package com.gnoemes.shikimori.entity.app.domain

data class Token(
        val authToken: String,
        val refreshToken: String
)