package com.gnoemes.shikimori.domain.app

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationsService : FirebaseMessagingService() {

    override fun onMessageReceived(remote: RemoteMessage?) {
        super.onMessageReceived(remote)

        val remoteNotification = remote?.notification

        if (remoteNotification != null) {

            val manager = NotificationManagerCompat.from(applicationContext)

            val pendingIntent =
                    if (remote.data?.get("url") == null) null
                    else PendingIntent.getActivity(applicationContext, 0, Intent(Intent.ACTION_VIEW, remote.data?.get("url")?.toUri()), 0)

            val notification = NotificationCompat.Builder(applicationContext, getString(R.string.default_notification_channel_id))
                    .setContentTitle(remoteNotification.title)
                    .setContentText(remoteNotification.body)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

            manager.notify(0, notification)
        }
    }
}