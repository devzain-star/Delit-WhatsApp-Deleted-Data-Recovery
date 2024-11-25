package com.recover.deleted.messages.chat.recovery.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository
import com.recover.deleted.messages.chat.recovery.model.StatusModel

class StatusViewModel(private val repository: WhatsAppStatusRepository) : ViewModel() {

    fun getStatuses(): LiveData<List<StatusModel>> {
        return repository.getWhatsAppStatuses()
    }
}