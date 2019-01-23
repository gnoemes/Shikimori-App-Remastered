package com.gnoemes.shikimori.entity.series.domain

sealed class TranslationMenu {
    data class Download(val  id : Long) : TranslationMenu()
}