package com.recover.deleted.messages.chat.recovery.models

import android.graphics.drawable.Icon

data class ChatMessage(
    var ticker: String? = null,
    var name: String? = null,
    var text: String? = null,
    var date: String? = null,
    var time: Long = 0,
    var id: Int = 0,
    var count: Int = 0,
    var icon: Icon? = null
)