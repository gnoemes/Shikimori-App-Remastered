package com.gnoemes.shikimori.entity.series.domain

data class TranslationSetting(
        val animeId: Long,
        val lastAuthor: String?,
        val lastType : TranslationType?
)