package com.gnoemes.shikimori.entity.series.domain

import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

sealed class TranslationMenu {
    data class Download(val videos: List<TranslationVideo>) : TranslationMenu()
    data class Author(val author: String) : TranslationMenu()
}