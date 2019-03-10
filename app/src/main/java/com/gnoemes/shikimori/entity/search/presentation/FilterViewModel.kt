package com.gnoemes.shikimori.entity.search.presentation

data class FilterViewModel(
         val state : STATE,
         val value : String,
         val text : String
) {

    enum class STATE {
        DEFAULT,
        SELECTED,
        INVERTED
    }
}