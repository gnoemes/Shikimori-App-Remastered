package com.gnoemes.shikimori.domain.download

import com.gnoemes.shikimori.entity.download.DownloadVideoData
import io.reactivex.Completable

interface DownloadInteractor {

    fun downloadVideo(data : DownloadVideoData) : Completable
}