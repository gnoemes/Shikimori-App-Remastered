package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class NuumStreamsMetadataResponse(
    val result: Result
) {
    data class Result(
        @SerializedName("media_container_streams") val mediaContainerStreams: List<MediaContainerStream>
    ) {
        data class MediaContainerStream(
            @SerializedName("stream_media") val streamMedia: List<StreamMedia>
        ) {
            data class StreamMedia(
                @SerializedName("media_meta") val mediaMeta: MediaMeta
            ) {
                data class MediaMeta(
                    @SerializedName("media_archive_url") val mediaArchiveUrl: String
                )
            }
        }
    }
}