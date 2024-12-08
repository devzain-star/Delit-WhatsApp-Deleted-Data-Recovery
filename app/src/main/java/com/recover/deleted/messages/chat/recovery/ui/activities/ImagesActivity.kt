package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.PhotoAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityImagesBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.Constants
import com.recover.deleted.messages.chat.recovery.utils.PathDirectories
import com.recover.deleted.messages.chat.recovery.utils.Utils
import com.recover.deleted.messages.chat.recovery.utils.fileComparator
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.Arrays
import java.util.Collections

class ImagesActivity : BaseActivity() {

    private lateinit var binding: ActivityImagesBinding
    private lateinit var emptyLay: RelativeLayout
    private lateinit var myAdapter: PhotoAdapter
    private val whatsapp_image = MutableLiveData<List<StatusModel>>()
    private lateinit var dirImages: File
    private val TAG = "ImagesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.images))
        setInsets()

        if (permissions.isStorageAllow()) {
            movePhotoData()
        } else requestPermissions(Constants.STORAGE_PERMISSIONS, Constants.REQUEST_PERMISSIONS)

        whatsapp_image.observe(this, Observer {
            if (it.isEmpty()) {
                emptyLay.visibility = View.VISIBLE
            } else {
                emptyLay.visibility = View.GONE
            }
            myAdapter = PhotoAdapter(it, this)
            binding.contentRecycler.adapter = myAdapter
        })
    }

    fun setInsets(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun movePhotoData() {
        Log.d(TAG, "start: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val cloneList: MutableList<StatusModel> = mutableListOf()
                // Define the folder path
                val folderPath =
                    "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images"
                val contentResolver = this.contentResolver
                val projection = arrayOf(
                    MediaStore.Images.Media._ID, // Include file ID in projection
                    MediaStore.Images.Media.DATA
                )
                val selection = "${MediaStore.Images.Media.DATA} LIKE '$folderPath%'"
                // Change this to match your directory
                val cursor = contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    "${MediaStore.Images.Media.DATE_ADDED} DESC"
                )

                cursor?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val id =
                            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                        val filepath =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        val statusModel = StatusModel().apply {
                            this.filepath = filepath
                            this.id = id
                        } // Create StatusModel object with filepath and selected status
                        cloneList.add(statusModel)
                    }
                }
                whatsapp_image.postValue(cloneList)
            } catch (_: Exception) {
            }
        } else {
            try {
                val file: File = PathDirectories.getWhatsappImages()
                val listFiles = listOf(*file.listFiles() as Array<out File>)
                Collections.sort(listFiles, fileComparator())
                var check = false
                for (pathname in listFiles) {
                    if (check)
                        break
                    if (pathname.isFile) {
                        Utils.compareDates(prefManager.getDate(), pathname.lastModified()).let {
                            if (it) {
                                val destinationFile = File(dirImages.path + "/" + pathname.name)
                                if (!destinationFile.exists()) {
                                    try {
                                        copyFile(pathname, destinationFile)
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                            } else
                                check = true
                        }
                    }
                }
                getFromOwnDirectory()
            } catch (_: Exception) {
            }
        }
    }

    private fun getFromOwnDirectory() {
        try {
            val cloneList: MutableList<StatusModel> = ArrayList()
            if (dirImages.isDirectory) {
                val listFile = dirImages.listFiles()
                listFile?.let {
                    Arrays.sort(it, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
                    for (value in it) {
                        if (value.isFile) {
                            val model = StatusModel()
                            model.filepath = value.absolutePath
                            cloneList.add(model)
                        }
                    }
                }
            }
            whatsapp_image.postValue(cloneList)
        } catch (_: Exception) {
        }
    }

    private fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.parentFile?.exists()!!)
        {
            if (!destFile.parentFile?.mkdirs()!!) {
                Log.e(TAG, "Failed to create parent directory: ${destFile.parentFile.absolutePath}")
                return
            }
        }

        if (!destFile.exists()) {
            if (!destFile.createNewFile()) {
                Log.e(TAG, "Failed to create file: ${destFile.absolutePath}")
                return
            }
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }
}