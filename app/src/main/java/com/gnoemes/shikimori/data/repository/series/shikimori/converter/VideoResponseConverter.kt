package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import io.reactivex.functions.Function

interface VideoResponseConverter : Function<VideoResponse, Video>