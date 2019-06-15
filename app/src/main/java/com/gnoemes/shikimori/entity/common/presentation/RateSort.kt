package com.gnoemes.shikimori.entity.common.presentation

import java.io.Serializable

//TODO generic sort interface for different lists
sealed class RateSort(val order: Int) : Serializable {
    object Name : RateSort(0)
    object Progress : RateSort(1)
    object DateAired : RateSort(2)
    object Score : RateSort(3)
    object Episodes : RateSort(4)

    object Id : RateSort(-1)
    object Type : RateSort(-1)
    object Status : RateSort(-1)

    companion object {
        fun values(): List<RateSort> = listOf(Name, Progress, DateAired, Score, Episodes)
    }
}