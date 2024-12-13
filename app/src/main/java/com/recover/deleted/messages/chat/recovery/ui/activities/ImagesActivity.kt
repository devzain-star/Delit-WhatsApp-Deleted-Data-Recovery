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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emptyLayout = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.images))

        // Initialize DeletedMediaManager
        deletedMediaManager = DeletedMediaManager(this)

        // Load Images
        setupRecyclerView()
        loadDeletedImages()
    }

    private fun loadDeletedImages() {
        val appInstallTime = packageManager.getPackageInfo(packageName, 0).firstInstallTime
        val imagesDir = File(getExternalFilesDir(null), "${getString(R.string.app_name)}/Images")
        val imageFiles = imagesDir.listFiles()?.filter {
            it.isFile && it.lastModified() >= appInstallTime
        }?.sortedByDescending { it.lastModified() } ?: emptyList()

        if (imageFiles.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.GONE
            val imageModels = imageFiles.map { file ->
                StatusModel().apply {
                    filepath = file.absolutePath
                    type = "image"
                }
            }
            adapter = PhotoAdapter(imageModels, this)
            binding.contentRecycler.adapter = adapter
        }
    }



    private fun setupRecyclerView() {
        adapter = PhotoAdapter(emptyList(),this)

        binding.contentRecycler.apply {
            layoutManager = GridLayoutManager(this@ImagesActivity, 3) // 3 columns
            adapter = adapter
        }
    }
}




