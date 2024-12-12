package com.recover.deleted.messages.chat.recovery.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.data.WhatsAppImageManager

class DataTransferService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform your task here
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "foreground_service_channel"
            val channelName = "Foreground Service"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)

            val notification = Notification.Builder(this, channelId)
                .setContentTitle("Service Running")
                .setContentText("Delit service is running")
                .setSmallIcon(R.drawable.notification_app_logo) // Use a valid icon
                .build()

            startForeground(1, notification)
        } else {
            val notification = NotificationCompat.Builder(this)
                .setContentTitle("Service Running")
                .setContentText("Delit service is running in the background")
                .setSmallIcon(R.drawable.notification_app_logo) // Use a valid icon
                .build()

            startForeground(1, notification)
        }
    }
}
