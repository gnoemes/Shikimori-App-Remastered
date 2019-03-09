package com.gnoemes.shikimori.entity.search.presentation

import com.gnoemes.shikimori.entity.search.domain.FilterType

data class FilterWithButtonsViewModel(
        val categoryLocalised : String,
        val type : FilterType,
        val filters : List<FilterViewModel>,
        val hasDelete : Boolean,
        val hasInvert : Boolean,
        val hasSelectAll : Boolean
)