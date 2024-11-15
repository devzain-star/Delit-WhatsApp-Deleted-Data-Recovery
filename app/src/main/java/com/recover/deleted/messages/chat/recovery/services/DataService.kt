package com.recover.deleted.messages.chat.recovery.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.utils.PathDirectories
import java.io.File
import java.util.Arrays
import org.apache.commons.io.comparator.LastModifiedFileComparator

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

    //Whatsapp Status
    fun getWhatsappStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val list = this.contentResolver.persistedUriPermissions
                if (list.isNotEmpty()) {
                    val file = DocumentFile.fromTreeUri(this, list[0].uri)
                    val cloneList: MutableList<StatusModel> = ArrayList()
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
                    whatsappStatusList.postValue(cloneList)
                }
            } catch (_: Exception) {
            }
        } else {
            try {
                val file: File = PathDirectories.getWhatsappStatusFolder()
                val cloneList: MutableList<StatusModel> = ArrayList()
                if (file.isDirectory) {
                    val listFile = file.listFiles()
                    if (listFile != null) {
                        Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
                        for (i in listFile) {
                            if (!i.name.equals(".nomedia")) {
                                if (i.isFile) {
                                    val model = StatusModel()
                                    Log.d("check_tag", "CheckDataServices: $i")
                                    model.filepath = i.toUri().toString()
                                    cloneList.add(model)
                                }
                            }
                        }
                    }
                }
                //    Log.d(TAG, "size is: " + cloneList.size)
                whatsappStatusList.postValue(cloneList)
            } catch (_: Exception) {
            }
        }
    }

}