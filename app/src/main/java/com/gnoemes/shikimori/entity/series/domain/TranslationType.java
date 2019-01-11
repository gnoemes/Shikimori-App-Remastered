package com.gnoemes.shikimori.entity.series.domain;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public enum TranslationType {
    @SerializedName("fandub")
    VOICE_RU("fandub", "озвучка"),
    @SerializedName("subtitles")
    SUB_RU("subtitles", "субтитры"),
    @SerializedName("raw")
    RAW("raw", "оригинал"),
    @SerializedName("all")
    ALL("all", "не выбрано");

    @Nullable
    private final String type;
    private final String localizedType;

    TranslationType(@Nullable String type, String localizedType) {
        this.type = type;
        this.localizedType = localizedType;
    }

    public boolean isEqualType(@Nullable String otherType) {
        return (type != null && type.equals(otherType))
                || (otherType == null && type == null);
    }

    @Nullable
    public String getType() {
        return type;
    }

    public String getLocalizedType() {
        return localizedType;
    }
}
