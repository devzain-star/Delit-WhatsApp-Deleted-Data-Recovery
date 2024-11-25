package com.recover.deleted.messages.chat.recovery.data

import android.content.Context
import android.os.Build
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PathDirectories

class WhatsAppStatusRepository(private val context: Context) {

    fun getWhatsAppStatuses(): LiveData<List<StatusModel>> {
        val liveData = MutableLiveData<List<StatusModel>>()
        val statusList = mutableListOf<StatusModel>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val persistedUris = context.contentResolver.persistedUriPermissions
            if (persistedUris.isNotEmpty()) {
                val documentFile = DocumentFile.fromTreeUri(context, persistedUris[0].uri)
                documentFile?.listFiles()?.filter { it.isFile && it.name != ".nomedia" }?.forEach {
                    val status = StatusModel().apply {
                        filepath = it.uri.toString()
                        type = if (it.type?.contains("image") == true) "image" else "video"
                        selected = false
                    }
                    statusList.add(status)
                }
            }
        } else {
            val statusDir = PathDirectories.getWhatsappStatusFolder()
            statusDir.listFiles()?.filter { it.isFile && it.name != ".nomedia" }
                ?.sortedByDescending { it.lastModified() }?.forEach {
                    val status = StatusModel().apply {
                        filepath = it.toUri().toString()
                        type = if (it.extension in listOf("jpg", "jpeg", "png")) "image" else "video"
                        selected = false
                    }
                    statusList.add(status)
                }
        }

        liveData.postValue(statusList)
        return liveData
    }
}