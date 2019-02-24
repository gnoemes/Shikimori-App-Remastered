package com.gnoemes.shikimori.utils

import android.graphics.Bitmap
import com.gnoemes.shikimori.entity.series.domain.VideoHosting



object Utils {

    fun isHostingSupports(hosting: VideoHosting, includeDownload: Boolean = false) : Boolean{
        val supports = mutableListOf(
                VideoHosting.SIBNET,
                VideoHosting.VK,
                VideoHosting.SOVET_ROMANTICA
        )

        //need subscription
        if (includeDownload) supports.add(VideoHosting.SMOTRET_ANIME)

        return supports.contains(hosting)
    }

    fun getDominantColor(bitmap: Bitmap): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

}