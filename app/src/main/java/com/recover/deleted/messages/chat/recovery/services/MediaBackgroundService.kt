package com.recover.deleted.messages.chat.recovery.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.data.DeletedMediaManager

class MediaBackgroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)

        val mediaManager = DeletedMediaManager(this)
        Thread {
            mediaManager.scanDeletedMedia()
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "MediaScanChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Media Scan",
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Delit data sync")
            .setContentText("Delit service is running in background")
            .setSmallIcon(R.drawable.notification_app_logo)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
