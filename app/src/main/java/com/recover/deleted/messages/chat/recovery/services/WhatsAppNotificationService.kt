package com.recover.deleted.messages.chat.recovery.services

import android.graphics.drawable.Icon
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.models.ChatMessage

class WhatsAppNotificationService : NotificationListenerService() {

    private val chatMessages = MutableLiveData<List<ChatMessage>>()
    private val messagesList = mutableListOf<ChatMessage>()

    companion object {
        const val WHATSAPP_PACKAGE = "com.whatsapp"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            if (it.packageName == WHATSAPP_PACKAGE) {
                val extras = it.notification.extras

                val ticker = it.notification.tickerText?.toString()
                val name = extras.getString("android.title") // Sender's name
                val text = extras.getString("android.text") // Message content
                val date = extras.getString("android.subText") // Optional
                val icon = extras.getParcelable<Icon>("android.largeIcon")
                val time = it.postTime

                if (!text.isNullOrEmpty() && !name.isNullOrEmpty()) {
                    val chatMessage = ChatMessage(
                        ticker = ticker,
                        name = name,
                        text = text,
                        date = date,
                        time = time,
                        icon = icon,
                        id = sbn.id,
                        count = 1
                    )
                    messagesList.add(chatMessage)
                    chatMessages.postValue(messagesList)
                }
            }
        }
    }

    fun getChatMessages(): LiveData<List<ChatMessage>> = chatMessages
}