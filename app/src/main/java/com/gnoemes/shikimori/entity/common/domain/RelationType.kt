package com.gnoemes.shikimori.entity.common.domain

import com.google.gson.annotations.SerializedName

enum class RelationType {
    @SerializedName("sequel")
    SEQUEL,
    @SerializedName("prequel")
    PREQUEL,
    @SerializedName("summary")
    SUMMARY,
    @SerializedName("side_story")
    SIDE_STORY,
    @SerializedName("parent_story")
    PARENT_STORY,
    @SerializedName("alternative_setting")
    ALTERNATIVE_SETTING,
    @SerializedName("alternative_version")
    ALTERNATIVE_VERSION,
    @SerializedName("other")
    OTHER,
    @SerializedName("full_story")
    FULL_STORY,
}