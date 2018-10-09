package com.gnoemes.shikimori.entity.forum.domain

import com.google.gson.annotations.SerializedName

enum class ForumType {
    @SerializedName("all")
    ALL,
    @SerializedName("news")
    NEWS,
    @SerializedName("animanga")
    ANIME_AND_MANGA,
    @SerializedName("vn")
    VISUAL_NOVELS,
    @SerializedName("games")
    GAMES,
    @SerializedName("site")
    SITE,
    @SerializedName("offtopic")
    OFF_TOPIC,
    @SerializedName("clubs")
    CLUBS,
    @SerializedName("reviews")
    REVIEWS,
    @SerializedName("contests")
    CONTESTS,
    @SerializedName("collections")
    COLLECTIONS,
    @SerializedName("cosplay")
    COSPLAY,
    @SerializedName("my_clubs")
    MY_CLUBS;
}