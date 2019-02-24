package com.gnoemes.shikimori.data.local.services.impl

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.data.local.services.DownloadSource
import com.gnoemes.shikimori.entity.download.DownloadVideoData
import com.gnoemes.shikimori.utils.downloadManager
import com.gnoemes.shikimori.utils.toUri
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class DownloadManagerSourceImpl @Inject constructor(
        private val context: Context,
        private val settingsSource: SettingsSource
) : DownloadSource {

    override fun downloadVideo(data: DownloadVideoData): Completable {
        return if (data.link.isNullOrBlank()) Completable.error(NoSuchElementException())
        else Completable.fromAction {
            val title = String.format(context.getString(R.string.episode_number), data.episodeIndex).plus(" ${data.animeName}")

            val file = File(settingsSource.downloadFolder)
            val path = Uri.withAppendedPath(Uri.fromFile(file), "anime/${data.animeName}/$title.mp4")

            val manager = context.downloadManager()
            val request = DownloadManager.Request(data.link.toUri())
                    .setTitle(title)
                    .setDescription(context.getString(R.string.app_name))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .apply {
                        allowScanningByMediaScanner()
                        setDestinationUri(path)
                        data.requestHeaders.entries.forEach { addRequestHeader(it.key, it.value) }
                    }

            manager?.enqueue(request)
        }
    }
}