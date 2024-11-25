package com.recover.deleted.messages.chat.recovery.data


import android.content.Context
import android.os.Build
import android.util.Log
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
                    statusList.add(StatusModel(filepath = it.uri.toString()))
                }
            }
        } else {
            val statusDir = PathDirectories.getWhatsappStatusFolder()
            statusDir.listFiles()?.filter { it.isFile && it.name != ".nomedia" }
                ?.sortedByDescending { it.lastModified() }?.forEach {
                    statusList.add(StatusModel(filepath = it.toUri().toString()))
                }
        }

        liveData.postValue(statusList)
        return liveData
    }
}