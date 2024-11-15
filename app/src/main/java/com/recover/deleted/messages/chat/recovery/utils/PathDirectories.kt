package com.recover.deleted.messages.chat.recovery.utils

import android.os.Environment
import android.util.Log
import java.io.File

class PathDirectories {

    companion object{
        val MINI_KIND = 1
        val MICRO_KIND = 3
        val GRID_COUNT = 2
        var APP_DIR: String? = null
        private const val TAG = "PathDirectories"
        private var WHATSAPP : String = "WhatsApp"

        val STATUS_DIRECTORY = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator + "$WHATSAPP/Media/.Statuses"
        )

        val STATUS_DIRECTORY_NEW = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/$WHATSAPP/Media/.Statuses")
        )

        val VOICE_DIRECTORY = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "$WHATSAPP/Media/$WHATSAPP Voice Notes")
        )

        val VOICE_DIRECTORY_NEW = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/$WHATSAPP/Media/$WHATSAPP Voice Notes")
        )

        val AUDIO_DIRECTORY = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "$WHATSAPP/Media/$WHATSAPP Audio")
        )

        val AUDIO_DIRECTORY_NEW = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/$WHATSAPP/Media/$WHATSAPP Audio")
        )

        val DOC_DIRECTORY = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "$WHATSAPP/Media/$WHATSAPP Documents")
        )

        val DOC_DIRECTORY_NEW = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/$WHATSAPP/Media/$WHATSAPP Documents")
        )

        fun getWhatsappImages(): File {
            return if (File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + "$WHATSAPP Images"
                ).isDirectory
            ) {
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + "$WHATSAPP Images"
                )
            } else {
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "$WHATSAPP" + File.separator + "Media" + File.separator + "$WHATSAPP Images"
                )
            }
        }

        fun getWhatsappVideosFolder(): File {
            return if (File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + "$WHATSAPP Video"
                ).isDirectory
            ) {
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + "$WHATSAPP Video"
                )
            } else {
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + WHATSAPP + File.separator + "Media" + File.separator + "$WHATSAPP Video"
                )
            }
        }

        fun getWhatsappStatusFolder(): File {
            return if (File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + ".Statuses"
                ).isDirectory
            ) {
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "Android/media/com.whatsapp/$WHATSAPP" + File.separator + "Media" + File.separator + ".Statuses"
                )
            } else {
                File(
                    Environment.getExternalStorageDirectory() , File.separator + "$WHATSAPP/Media/.Statuses"
                )
            }
        }

    }
}