package com.gnoemes.shikimori.data.repository.download

import com.gnoemes.shikimori.data.local.services.DownloadSource
import com.gnoemes.shikimori.entity.download.DownloadVideoData
import io.reactivex.Completable
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
        private val source : DownloadSource
): DownloadRepository {

    override fun downloadVideo(data: DownloadVideoData): Completable = source.downloadVideo(data)
}