package com.recover.deleted.messages.chat.recovery.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository

class StatusViewModelFactory(
    private val repository: WhatsAppStatusRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
            return StatusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}