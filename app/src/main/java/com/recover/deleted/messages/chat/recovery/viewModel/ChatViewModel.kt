package com.recover.deleted.messages.chat.recovery.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recover.deleted.messages.chat.recovery.models.ChatMessage

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    fun addMessage(message: ChatMessage) {
        val updatedMessages = _messages.value?.toMutableList() ?: mutableListOf()
        updatedMessages.add(0, message) // Add to the top
        _messages.value = updatedMessages
    }
}