package com.recover.deleted.messages.chat.recovery.services

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import com.recover.deleted.messages.chat.recovery.R
import java.io.File

class DataService: Service() {

    private val TAG = "DataService"
    private lateinit var dirImages: File
    private lateinit var dirVideos: File
    private lateinit var dirDownloadPhotos: File
    private lateinit var dirDownloadVideos: File
    var prefManager: PreferencesManager? = null
    private var isPhotosMoved = false
    private var isVideosMoved = false


    companion object {
        val whatsapp_image = MutableLiveData<List<StatusModel>>()
        val whatsapp_videos = MutableLiveData<List<StatusModel>>()
        val whatsapp_status = MutableLiveData<List<StatusModel>>()
        val whatsapp_status_download_photos = MutableLiveData<List<StatusModel>>()
        val whatsapp_status_download_videos = MutableLiveData<List<StatusModel>>()
    }

    override fun onCreate() {
        super.onCreate()
        makeDelitDirectories()
        prefManager = PreferencesManager(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun makeDelitDirectories() {
        val sd = Environment.getExternalStorageDirectory()

        dirImages = File(sd, File.separator + getString(R.string.app_name) + "/Images/")
        if (!dirImages.exists()) dirImages.mkdirs()

        dirVideos = File(sd, File.separator + getString(R.string.app_name) + "/Videos/")
        if (!dirVideos.exists()) dirVideos.mkdirs()

        dirDownloadPhotos = File(
            sd,
            File.separator + "Download" + File.separator + getString(R.string.app_name) + "/Images/"
        )
        if (!dirDownloadPhotos.exists()) dirDownloadPhotos.mkdirs()

        dirDownloadVideos = File(
            sd,
            File.separator + "Download" + File.separator + getString(R.string.app_name) + "/Videos/"
        )
        if (!dirDownloadVideos.exists()) dirDownloadVideos.mkdirs()
    }

}