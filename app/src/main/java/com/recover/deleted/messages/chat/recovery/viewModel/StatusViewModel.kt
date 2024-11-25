package com.recover.deleted.messages.chat.recovery.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository
import com.recover.deleted.messages.chat.recovery.model.StatusModel

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WhatsAppStatusRepository(application.applicationContext)

    val whatsappStatuses: LiveData<List<StatusModel>> = repository.getWhatsAppStatuses()
}