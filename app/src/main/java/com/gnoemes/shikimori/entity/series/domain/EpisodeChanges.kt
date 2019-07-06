package com.gnoemes.shikimori.entity.series.domain


sealed class EpisodeChanges {
    object Success : EpisodeChanges()
    data class Error(val exception : Throwable) : EpisodeChanges()
    data class Changes(val rateId: Long, val animeId: Long, val episodeIndex: Int, val isWatched: Boolean) : EpisodeChanges()
}
