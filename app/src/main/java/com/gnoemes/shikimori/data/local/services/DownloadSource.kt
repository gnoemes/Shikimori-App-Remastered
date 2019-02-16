package com.gnoemes.shikimori.data.local.services

import com.gnoemes.shikimori.entity.download.DownloadVideoData
import io.reactivex.Completable

interface DownloadSource  {

    fun downloadVideo(data : DownloadVideoData) : Completable
}