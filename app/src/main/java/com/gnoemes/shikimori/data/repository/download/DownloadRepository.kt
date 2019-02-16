package com.gnoemes.shikimori.data.repository.download

import com.gnoemes.shikimori.entity.download.DownloadVideoData
import io.reactivex.Completable

interface DownloadRepository {

    fun downloadVideo(data : DownloadVideoData) : Completable
}