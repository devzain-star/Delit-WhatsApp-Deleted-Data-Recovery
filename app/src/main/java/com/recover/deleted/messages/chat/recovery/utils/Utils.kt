package com.recover.deleted.messages.chat.recovery.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.webkit.MimeTypeMap

import com.recover.deleted.messages.chat.recovery.R
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URLConnection
import kotlin.text.startsWith

class Utils {

    companion object {
        private const val VIDEO_EXTENSIONS = "mp4|webm|ogg|mpK|avi|mkv|flv|mpg|wmv|vob|ogv|mov|qt|rm|rmvb|asf|m4p|m4v|mp2|mpeg|mpe|mpv|m2v|3gp|f4p|f4a|f4b|f4v"

        fun mediaScanner(context: Context, filePath: String, mimeType: String) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(filePath),
                arrayOf(mimeType),
                null
            )
        }

        fun getMimeType(file: File): String {
            val extension = file.extension
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
        }

        fun isVideoFile(path: String?): Boolean {
            return path?.let {
                it.lowercase().matches(".*\\.($VIDEO_EXTENSIONS)$".toRegex())
            } ?: false
        }

        fun isImageFile(path: String?): Boolean {
            val mimeType = URLConnection.guessContentTypeFromName(path)
            return mimeType?.startsWith("image") == true
        }

        fun downloadFile(context: Context, sourcePath: String?): Boolean {
            return try {
                val destinationDir = if (isImageFile(sourcePath)) {
                    getDir(context, "Images")
                } else {
                    getDir(context, "Videos")
                }
                sourcePath?.let { source ->
                    val sourceFile = File(source)
                    FileUtils.copyFileToDirectory(sourceFile, destinationDir)
                    mediaScanner(
                        context,
                        File(destinationDir, sourceFile.name).absolutePath,
                        if (isImageFile(source)) "image/*" else "video/*"
                    )
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun getDir(context: Context, folder: String): File {
            val rootDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "${context.getString(R.string.app_name)}/$folder"
            )
            if (!rootDir.exists()) rootDir.mkdirs()
            return rootDir
        }

        fun getCurrentTimestamp(): Long {
            return System.currentTimeMillis()
        }


        fun compareDates(startDate: Long?, fileDate: Long): Boolean {
            return fileDate> startDate!!
        }
    }
}
