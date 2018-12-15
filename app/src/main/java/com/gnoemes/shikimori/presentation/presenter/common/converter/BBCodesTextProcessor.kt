package com.gnoemes.shikimori.presentation.presenter.common.converter

interface BBCodesTextProcessor {
    fun process(source: CharSequence): CharSequence
    fun process(source: String): String
}