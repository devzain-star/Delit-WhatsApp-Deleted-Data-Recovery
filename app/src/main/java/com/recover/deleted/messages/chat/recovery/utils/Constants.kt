package com.recover.deleted.messages.chat.recovery.utils

import com.recover.deleted.messages.chat.recovery.models.App

object Constants {
    const val DELIT_PREFS = "delit_prefs"
    const val DELIT_STATUS_PREFS = "status_folder_uri"


    const val REQUEST_CODE_UPDATE = 1001
    const val PERMISSION_DIALOG_TITLE = "permission_dialog_title"
    const val PERMISSION_DIALOG_MSG = "permission_dialog_msg"
    const val PERMISSION_DIALOG_DENIED_TITLE = "permission_dialog_denied_title"
    const val PERMISSION_DIALOG_DENIED_MSG = "permission_dialog_denied_msg"
    const val PERMISSION_DIALOG_DENIED = "permission_dialog_denied"
    const val LOGS_DB_NAME = "logs_messages_db"
    const val NOTIFICATION_CHANNEL_ID = "delit"
    const val NOTIFICATION_CHANNEL_NAME = "delit_channel"

    enum class EnabledAppsDisplayType{
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Set of apps this app can auto reply
     */
    @JvmField
    val SUPPORTED_APPS: Set<App> = setOf(
        App("WhatsApp", "com.whatsapp"),
    )

    const val MIN_DAYS = 0
    const val MAX_DAYS = 7
    const val MIN_REPLIES_TO_ASK_APP_RATING = 5
    const val EMAIL_ADDRESS = "delit@revviz.com"
    const val EMAIL_SUBJECT = "Delit-Feedback"

}