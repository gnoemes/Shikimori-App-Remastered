package com.gnoemes.shikimori.presentation.presenter.similar.converter

import com.gnoemes.shikimori.entity.similar.presentation.SimilarViewModel

interface SimilarViewModelConverter {
    fun apply(it: List<Any>, isGuest: Boolean): List<SimilarViewModel>
}