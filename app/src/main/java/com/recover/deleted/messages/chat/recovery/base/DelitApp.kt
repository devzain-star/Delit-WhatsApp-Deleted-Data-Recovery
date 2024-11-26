package com.recover.deleted.messages.chat.recovery.base

import android.app.Application
import com.recover.deleted.messages.chat.recovery.R
import android.os.Environment
import java.io.File

class DelitApp : Application() {

    override fun onCreate() {
        super.onCreate()
        makeDelitDirectories()
    }

    private fun makeDelitDirectories() {
        val sd = Environment.getExternalStorageDirectory()

        val dirImages = File(sd, File.separator + getString(R.string.app_name) + "/Images/")
        if (!dirImages.exists()) dirImages.mkdirs()

        val dirVideos = File(sd, File.separator + getString(R.string.app_name) + "/Videos/")
        if (!dirVideos.exists()) dirVideos.mkdirs()

        val dirDownloadPhotos = File(
            sd,
            File.separator + "Download" + File.separator + getString(R.string.app_name) + "/Images/"
        )
        if (!dirDownloadPhotos.exists()) dirDownloadPhotos.mkdirs()

        val dirDownloadVideos = File(
            sd,
            File.separator + "Download" + File.separator + getString(R.string.app_name) + "/Videos/"
        )
        if (!dirDownloadVideos.exists()) dirDownloadVideos.mkdirs()
    }

}