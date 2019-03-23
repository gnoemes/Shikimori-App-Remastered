package com.gnoemes.shikimori.entity.common.presentation

//TODO generic sort interface for different lists
sealed class RateSort {
    object Id : RateSort()
    object Type : RateSort()
    object DateAired : RateSort()
    object Status : RateSort()
    object Episodes : RateSort()
    object EpisodesWatched : RateSort()
    object Score : RateSort()
    object Name : RateSort()
}