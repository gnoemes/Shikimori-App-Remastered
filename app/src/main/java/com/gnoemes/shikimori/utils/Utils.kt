package com.gnoemes.shikimori.utils

import com.gnoemes.shikimori.entity.series.domain.VideoHosting

object Utils {

    fun isHostingSupports(hosting: VideoHosting, includeDownload: Boolean = false) : Boolean{
        val supports = mutableListOf(
                VideoHosting.SIBNET,
                VideoHosting.VK
        )

        if (includeDownload) supports.add(VideoHosting.SMOTRET_ANIME)

        return supports.contains(hosting)

    }
}