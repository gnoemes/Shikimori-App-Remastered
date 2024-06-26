package com.gnoemes.shikimori.entity.common.domain

import com.google.gson.annotations.SerializedName

enum class AgeRating(val rating: String, val isR18: Boolean = false) {
    @SerializedName("none")
    NONE("none"),
    @SerializedName("g")
    G("g"),
    @SerializedName("pg")
    PG("pg"),
    @SerializedName("pg_13")
    PG_13("pg_13"),
    @SerializedName("r")
    R("r"),
    @SerializedName("r_plus")
    R_PLUS("r_plus"),
    @SerializedName("rx")
    RX("rx", true)
}