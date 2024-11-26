package com.recover.deleted.messages.chat.recovery.data

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PathDirectories

class WhatsAppStatusRepository(private val context: Context) {

    fun getWhatsAppStatusesFromUri(uri: Uri): LiveData<List<StatusModel>> {
        val liveData = MutableLiveData<List<StatusModel>>()
        val statusList = mutableListOf<StatusModel>()

        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.listFiles()?.filter { it.isFile && it.name != ".nomedia" }?.forEach {
            val status = StatusModel().apply {
                filepath = it.uri.toString()
                type = if (it.type?.contains("image") == true) "image" else "video"
                selected = false
            }
            statusList.add(status)
        }

        liveData.postValue(statusList)
        return liveData
    }
}