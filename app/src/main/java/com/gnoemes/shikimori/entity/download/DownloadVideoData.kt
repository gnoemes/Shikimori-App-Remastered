package com.gnoemes.shikimori.entity.download

data class DownloadVideoData(
        val animeId : Long,
        val animeName : String,
        val episodeIndex : Int,
        val link : String?
)