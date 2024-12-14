package com.recover.deleted.messages.chat.recovery.data

import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import java.io.File
import com.recover.deleted.messages.chat.recovery.R
import java.io.IOException

class DeletedMediaManager(private val context: Context) {

    val dirImages: File = File(context.getExternalFilesDir(null), "${context.getString(R.string.app_name)}/Images")
    val dirVideos: File = File(context.getExternalFilesDir(null), "${context.getString(R.string.app_name)}/Videos")

    init {
        if (!dirImages.exists()) dirImages.mkdirs()
        if (!dirVideos.exists()) dirVideos.mkdirs()
    }

    fun scanWhatsAppMedia(type: String, targetDir: File) {
        val appInstallTime = getAppInstallTime()
        val mediaUri = if (type == "image") MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DATE_ADDED)
        val selection = "${MediaStore.MediaColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf("%WhatsApp%")

        val cursor = context.contentResolver.query(
            mediaUri, projection, selection, selectionArgs, "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val file = File(filePath)
                if (file.exists() && file.lastModified() >= appInstallTime && filePath !in getFileSet(type)) {
                    transferFileIfNeeded(file, targetDir)
                }
            }
        }
    }

    private fun transferFileIfNeeded(sourceFile: File, targetDir: File): File? {
        val targetFile = File(targetDir, sourceFile.name)
        return if (!targetFile.exists() && sourceFile.exists()) {
            try {
                sourceFile.copyTo(targetFile, overwrite = false)
                addFileToDatabase(targetFile.absolutePath, if (targetDir.name == "Images") "image" else "video")
                targetFile
            } catch (e: IOException) {
                Log.e("FileTransfer", "Error copying file: ${e.message}")
                null
            }
        } else null
    }

    private fun addFileToDatabase(filePath: String, type: String) {
        val sharedPreferences = context.getSharedPreferences("DeletedFiles", Context.MODE_PRIVATE)
        sharedPreferences.edit().putStringSet(type, getFileSet(type).plus(filePath)).apply()
    }

    fun getFileSet(type: String): MutableSet<String> {
        val sharedPreferences = context.getSharedPreferences("DeletedFiles", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(type, emptySet())?.toMutableSet() ?: mutableSetOf()
    }

    private fun getAppInstallTime(): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            System.currentTimeMillis()
        }
    }
}