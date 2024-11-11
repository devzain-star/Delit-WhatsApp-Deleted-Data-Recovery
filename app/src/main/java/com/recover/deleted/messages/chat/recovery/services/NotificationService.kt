package com.recover.deleted.messages.chat.recovery.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationService: NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        //Todo: Handle notification received
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        //Todo: Handle notification removal
    }
}