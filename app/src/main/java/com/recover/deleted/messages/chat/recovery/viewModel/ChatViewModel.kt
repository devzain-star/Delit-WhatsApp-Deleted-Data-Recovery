package com.recover.deleted.messages.chat.recovery.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.recover.deleted.messages.chat.recovery.models.ChatMessage
import com.recover.deleted.messages.chat.recovery.services.WhatsAppNotificationService

class ChatViewModel : ViewModel() {

    private val chatMessages = WhatsAppNotificationService().getChatMessages()

    fun getChatMessages(): LiveData<List<ChatMessage>> = chatMessages
}