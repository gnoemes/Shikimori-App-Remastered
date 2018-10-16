package com.gnoemes.shikimori.entity.series.domain;

import androidx.annotation.Nullable;

public enum TranslationType {
    VOICE_RU("озвучка"),
    SUB_RU("субтитры"),
    RAW("оригинал"),
    ALL("не выбрано");

    @Nullable
    private final String type;

    TranslationType(@Nullable String type) {
        this.type = type;
    }

    public boolean isEqualType(@Nullable String otherType) {
        return (type != null && type.equals(otherType))
                || (otherType == null && type == null);
    }

    @Nullable
    public String getType() {
        return type;
    }
}
