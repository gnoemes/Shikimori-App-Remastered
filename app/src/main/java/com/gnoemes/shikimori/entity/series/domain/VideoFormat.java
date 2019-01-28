package com.gnoemes.shikimori.entity.series.domain;

import com.google.android.exoplayer2.util.MimeTypes;

public enum VideoFormat {
    MP4(MimeTypes.APPLICATION_MP4),
    DASH(MimeTypes.APPLICATION_MPD),
    HLS(MimeTypes.APPLICATION_M3U8),
    EMBEDDED_PLAYER("");

    private final String type;

    VideoFormat(String type) {
        this.type = type;
    }

    public boolean isEqualType(String otherType) {
        return this.type.equals(otherType);
    }

    public String getType() {
        return type;
    }
}
