package com.recover.deleted.messages.chat.recovery.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager

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
}