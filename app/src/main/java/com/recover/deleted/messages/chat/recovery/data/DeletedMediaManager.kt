package com.recover.deleted.messages.chat.recovery.data

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.services.MediaBackgroundService
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class DeletedMediaManager(private val context: Context) {

    private val dirImages: File = File(context.getExternalFilesDir(null), "${context.getString(R.string.app_name)}/Images")
    private val dirVideos: File = File(context.getExternalFilesDir(null), "${context.getString(R.string.app_name)}/Videos")

    init {
        if (!dirImages.exists()) dirImages.mkdirs()
        if (!dirVideos.exists()) dirVideos.mkdirs()
    }

    fun scanDeletedMedia() {
        val contentResolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            scanMedia(contentResolver, imageUri, dirImages)
            scanMedia(contentResolver, videoUri, dirVideos)
        } else {
            // For Android versions below Q
            val whatsAppDir = File(Environment.getExternalStorageDirectory(), "WhatsApp/Media")
            val imageDir = File(whatsAppDir, "WhatsApp Images")
            val videoDir = File(whatsAppDir, "WhatsApp Video")

            copyFilesFromDir(imageDir, dirImages)
            copyFilesFromDir(videoDir, dirVideos)
        }
    }

    private fun scanMedia(contentResolver: ContentResolver, uri: Uri, targetDir: File) {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_ADDED
        )

        val selection = "${MediaStore.MediaColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf("%WhatsApp%")

        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val sourceFile = File(filePath)

                if (sourceFile.exists() && !sourceFile.isDirectory) {
                    val destinationFile = File(targetDir, sourceFile.name)
                    if (!destinationFile.exists()) {
                        try {
                            copyFile(sourceFile, destinationFile)
                        } catch (e: IOException) {
                            Log.e("DeletedMediaManager", "Error copying file: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    private fun copyFilesFromDir(sourceDir: File, targetDir: File) {
        if (sourceDir.exists() && sourceDir.isDirectory) {
            sourceDir.listFiles()?.forEach { file ->
                if (file.isFile && !file.name.startsWith(".")) {
                    val destFile = File(targetDir, file.name)
                    if (!destFile.exists()) {
                        try {
                            copyFile(file, destFile)
                        } catch (e: IOException) {
                            Log.e("DeletedMediaManager", "Error copying file: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    private fun copyFile(sourceFile: File, destFile: File) {
        try {
            FileInputStream(sourceFile).use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.channel.transferTo(0, sourceFile.length(), outputStream.channel)
                }
            }
        } catch (e: Exception) {
            Log.e("DeletedMediaManager", "Error copying file: ${e.message}")
        }
    }

    fun startBackgroundScanning() {
        val intent = Intent(context, MediaBackgroundService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }
}

