package com.gnoemes.shikimori.entity.series.domain;

import com.google.gson.annotations.SerializedName;

public enum TranslationQuality {
    @SerializedName("bd")
    BD("bd"),
    @SerializedName(value = "tv", alternate = "web")
    TV("tv"),
    @SerializedName("dvd")
    DVD("dvd"),;


    private final String quality;

    TranslationQuality(String quality) {
        this.quality = quality;
    }

    public boolean equalQuality(String otherQuality) {
        return quality.equals(otherQuality);
    }

    public String getQuality() {
        return quality;
    }
}
