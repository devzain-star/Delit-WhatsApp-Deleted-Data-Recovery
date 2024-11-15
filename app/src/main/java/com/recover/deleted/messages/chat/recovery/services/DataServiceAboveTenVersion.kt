package com.recover.deleted.messages.chat.recovery.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import java.util.Arrays

class DataServiceAboveTenVersion : Service(){

    private val TAG = "DataServiceAboveTenVersion"
    var prefManager: PreferencesManager? = null

    companion object {
        val whatsAppStatus = MutableLiveData<List<StatusModel>>()
        val whatsAppImages = MutableLiveData<List<StatusModel>>()
        val whatsAppVideos = MutableLiveData<List<StatusModel>>()
    }

    override fun onCreate() {
        super.onCreate()
        prefManager = PreferencesManager(this)
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appContext = applicationContext
        getWhatsappStatus(appContext)
        return START_NOT_STICKY
    }

    private fun getWhatsappStatus(context: Context) {
        val list = context.contentResolver.persistedUriPermissions
        if (list.isNotEmpty()) {
            val file = DocumentFile.fromTreeUri(context, list[0].uri)
            Log.d("check_tag", "CheckDirectoryFile: " + file)
            val cloneList: MutableList<StatusModel> = java.util.ArrayList()

            if (file != null) {
                if (file.isDirectory) {
                    val listFile = file.listFiles()
                    Arrays.sort(listFile) { file1, file2 ->
                        file1.name!!.compareTo(file2.name!!)
                    }
                    for (i in listFile) {
                        if (!i.name.equals(".nomedia")) {
                            if (i.isFile) {
                                val model = StatusModel()
                                model.filepath = i.uri.toString()

                                cloneList.add(model)
                            }
                        }
                    }
                }
            }
            whatsAppStatus.postValue(cloneList)
        } else {
            // Handle case when the list is empty
            Log.d(TAG, "persistedUriPermissions list is empty")
        }
    }
}