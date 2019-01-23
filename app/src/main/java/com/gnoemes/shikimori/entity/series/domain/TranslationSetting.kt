package com.gnoemes.shikimori.entity.series.domain

data class TranslationSetting(
        val animeId: Long,
        val episodeIndex : Int,
        val lastAuthor: String?,
        val lastType : TranslationType?
)