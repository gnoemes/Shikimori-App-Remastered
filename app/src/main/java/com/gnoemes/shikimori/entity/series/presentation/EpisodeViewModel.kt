package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.series.domain.TranslationType

data class EpisodeViewModel(
        val id: Long,
        val index : Int,
        val animeId: Long,
        val types: List<TranslationType>,
        val state : State,
        val isWatched: Boolean
) {

    sealed class State {
        object NotChecked : State()
        object Loading : State()
        object Checked : State()
    }
}