package com.gnoemes.shikimori.entity.search.presentation

import com.gnoemes.shikimori.entity.search.domain.FilterType

data class FilterNestedViewModel(
        val categoryLocalised : String,
        val type : FilterType,
        val filters : List<FilterViewModel>,
        val appliedCount: Int
)
