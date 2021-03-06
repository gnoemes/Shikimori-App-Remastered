package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.series.domain.TranslationType

data class TranslationViewModel(
        val firstVideoId : Long,
        val type: TranslationType,
        val authors: String,
        val description: CharSequence?,
        val videos: List<TranslationVideo>,
        val isSameAuthor: Boolean,
        val canBeDownloaded : Boolean,
        val episodesSize : Int
)