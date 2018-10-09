package com.gnoemes.shikimori.entity.common.domain

import com.google.gson.annotations.SerializedName

enum class Type {
    @SerializedName("Anime")
    ANIME,
    @SerializedName("Manga")
    MANGA,
    @SerializedName("Ranobe")
    RANOBE,
    @SerializedName("Character")
    CHARACTER,
    @SerializedName("Person")
    PERSON,
    @SerializedName("User")
    USER,
    @SerializedName("Club")
    CLUB,
    @SerializedName("Collection")
    COLLECTION,
    @SerializedName("Review")
    REVIEW,
    @SerializedName("CosplayGallery")
    COSPLAY,
    @SerializedName("Contest")
    CONTEST,
    @SerializedName("")
    UNKNOWN
}