package com.gnoemes.shikimori.entity.common.presentation
//TODO generic or interface
data class SortItem(
        val currentSort : RateSort,
        val sorts : List<Pair<RateSort, String>>,
        val descending : Boolean
)