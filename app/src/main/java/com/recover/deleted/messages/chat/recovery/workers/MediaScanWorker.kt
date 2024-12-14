package com.recover.deleted.messages.chat.recovery.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.recover.deleted.messages.chat.recovery.data.DeletedMediaManager

class MediaScanWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val mediaManager = DeletedMediaManager(applicationContext)

        mediaManager.scanWhatsAppMedia("image", mediaManager.dirImages)
        mediaManager.scanWhatsAppMedia("video", mediaManager.dirVideos)

        return Result.success()
    }
}
