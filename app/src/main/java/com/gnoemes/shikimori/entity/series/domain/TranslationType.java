package com.gnoemes.shikimori.entity.series.domain;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public enum TranslationType {
    @SerializedName(value = "fandub", alternate = "dub")
    VOICE_RU("fandub", "озвучка"),
    @SerializedName(value = "subtitles", alternate = "sub")
    SUB_RU("subtitles", "субтитры"),
    @SerializedName("raw")
    RAW("raw", "оригинал"),
    @SerializedName("all")
    ALL("all", "не выбрано");

    private final String type;
    private final String localizedType;

    TranslationType(String type, String localizedType) {
        this.type = type;
        this.localizedType = localizedType;
    }

    public boolean isEqualType(@Nullable String otherType) {
        return type.equals(otherType) || localizedType.equals(otherType);
    }

    @Nullable
    public String getType() {
        return type;
    }

    public String getLocalizedType() {
        return localizedType;
    }
}
