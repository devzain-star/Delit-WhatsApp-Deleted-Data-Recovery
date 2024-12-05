package com.recover.deleted.messages.chat.recovery.services

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.recover.deleted.messages.chat.recovery.model.NotificationModel
import com.recover.deleted.messages.chat.recovery.sqlite.SqliteHelper
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import com.recover.deleted.messages.chat.recovery.utils.RefreshListener

class NotificationService: NotificationListenerService() {

    private val TAG = NotificationService::class.java.simpleName
    var sqliteHelper: SqliteHelper? = null
    var appPackage : String? = null
    var nTicker = ""
    var text = ""
    var title:String = ""
    var icon: Icon? = null
    var drawable: Drawable? = null
    var postTime: Long = 0
    var currentTime: Long = 0
    var checkTime: Boolean? = null
    val regx1 = "([0-9].(new).(messages))"
    val regx2 = "(.[0-9].(messages).)"
    lateinit var prefManager: PreferencesManager
    companion object{
        var refreshListener: RefreshListener? = null
        var notificationModels: MutableList<NotificationModel>? = null
    }



    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        //Todo: Handle notification received
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        //Todo: Handle notification removal
    }
}