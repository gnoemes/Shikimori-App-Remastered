package com.gnoemes.shikimori.domain.app

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationsService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val remoteNotification = message.notification

        if (remoteNotification != null) {

            val manager = NotificationManagerCompat.from(applicationContext)

            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val pendingIntent =
                    if (message.data["url"] == null) null
                    else PendingIntent.getActivity(applicationContext, 0, Intent(Intent.ACTION_VIEW, message.data["url"]?.toUri()), flags)

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