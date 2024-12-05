package com.recover.deleted.messages.chat.recovery.utils

import com.recover.deleted.messages.chat.recovery.model.ContactModel


interface RefreshListener {
    fun onRefresh(model: ContactModel?)
}