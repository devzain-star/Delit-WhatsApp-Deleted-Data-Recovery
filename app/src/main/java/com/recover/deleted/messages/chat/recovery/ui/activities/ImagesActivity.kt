package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.Intent
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.PhotoAdapter
import com.recover.deleted.messages.chat.recovery.adapters.StatusAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.data.DeletedMediaManager
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
    private lateinit var emptyLayout: RelativeLayout
    private lateinit var adapter: PhotoAdapter
    private lateinit var deletedMediaManager: DeletedMediaManager
    private val imageList = mutableListOf<StatusModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emptyLayout = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.images))
        val mediaManager = DeletedMediaManager(this)
        mediaManager.scanWhatsAppMedia("image", mediaManager.dirImages)

        // Initialize DeletedMediaManager
        deletedMediaManager = DeletedMediaManager(this)

        // Load Images
        setupRecyclerView()
        loadImages()
    }

    private fun loadImages() {
        val fileSet = DeletedMediaManager(this).getFileSet("image")
        val files = fileSet.map { filePath ->
            File(filePath).takeIf { it.exists() }?.let { file ->
                StatusModel().apply {
                    filepath = file.absolutePath
                    type = "image"
                }
            }
        }.filterNotNull()

        imageList.clear()
        imageList.addAll(files.sortedByDescending { File(it.filepath).lastModified() })
        binding.contentRecycler.adapter?.notifyDataSetChanged()
    }


    private fun setupRecyclerView() {
        adapter = PhotoAdapter(imageList,this)

        binding.contentRecycler.apply {
            layoutManager = GridLayoutManager(this@ImagesActivity, 3) // 3 columns
            adapter = adapter
        }
    }

}




