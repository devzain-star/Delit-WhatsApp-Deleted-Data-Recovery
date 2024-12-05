package com.recover.deleted.messages.chat.recovery.services

import android.app.Notification
import android.content.Intent
import android.graphics.drawable.Icon
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.recover.deleted.messages.chat.recovery.models.ChatMessage

class WhatsAppNotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            if (sbn.packageName == "com.whatsapp") {
                val extras = sbn.notification.extras
                val ticker = sbn.notification.tickerText?.toString()
                val name = extras.getString(Notification.EXTRA_TITLE)
                val text = extras.getString(Notification.EXTRA_TEXT)
                val time = sbn.postTime
                val icon = extras.getParcelable<Icon>(Notification.EXTRA_LARGE_ICON)
                val date = android.text.format.DateFormat.format("dd/MM/yyyy", time).toString()

                val message = ChatMessage(
                    ticker = ticker,
                    name = name,
                    text = text,
                    date = date,
                    time = time,
                    icon = icon
                )

                val intent = Intent("WhatsAppMessage")
                intent.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
        }
    }
}