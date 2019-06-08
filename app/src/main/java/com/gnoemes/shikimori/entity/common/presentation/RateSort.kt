package com.gnoemes.shikimori.entity.common.presentation

import java.io.Serializable

//TODO generic sort interface for different lists
sealed class RateSort(val order: Int) : Serializable {
    object Id : RateSort(0)
    object Name : RateSort(1)
    object Type : RateSort(2)
    object DateAired : RateSort(3)
    object Status : RateSort(4)
    object Episodes : RateSort(5)
    object EpisodesWatched : RateSort(6)
    object Score : RateSort(7)

    companion object {
        fun values(): List<RateSort> = listOf(Id, Name, Type, DateAired, Status, Episodes, EpisodesWatched, Score)
    }
}