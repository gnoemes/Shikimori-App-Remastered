package com.gnoemes.shikimori.domain.download

import com.gnoemes.shikimori.data.repository.download.DownloadRepository
import com.gnoemes.shikimori.entity.download.DownloadVideoData
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class DownloadInteractorImpl @Inject constructor(
        private val repository: DownloadRepository
) : DownloadInteractor {

    override fun downloadVideo(data: DownloadVideoData): Completable = repository.downloadVideo(data).applyErrorHandlerAndSchedulers()
}