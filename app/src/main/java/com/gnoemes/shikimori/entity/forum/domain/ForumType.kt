package com.gnoemes.shikimori.entity.forum.domain

import com.google.gson.annotations.SerializedName

enum class ForumType(val type : String) {
    @field:SerializedName("all") ALL("all"),
    @field:SerializedName("news", alternate = ["News"]) NEWS("news"),
    @field:SerializedName("animanga") ANIME_AND_MANGA("animanga"),
    @field:SerializedName("vn") VISUAL_NOVELS("vn") ,
    @field:SerializedName("games") GAMES("games"),
    @field:SerializedName("site") SITE("site") ,
    @field:SerializedName("offtopic") OFF_TOPIC("offtopic"),
    @field:SerializedName("clubs") CLUBS("clubs") ,
    @field:SerializedName("reviews") REVIEWS("reviews") ,
    @field:SerializedName("contests") CONTESTS("contests") ,
    @field:SerializedName("collections") COLLECTIONS("collections") ,
    @field:SerializedName("cosplay") COSPLAY("cosplay") ,
    @field:SerializedName("my_clubs") MY_CLUBS("my_clubs");
}