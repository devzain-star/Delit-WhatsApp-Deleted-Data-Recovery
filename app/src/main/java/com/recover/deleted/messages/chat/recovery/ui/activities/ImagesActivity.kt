package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.PhotoAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.data.DeletedMediaManager
import com.recover.deleted.messages.chat.recovery.databinding.ActivityImagesBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import java.io.File

class ImagesActivity : BaseActivity() {

    private lateinit var binding: ActivityImagesBinding
    private lateinit var adapter: PhotoAdapter
    private lateinit var deletedMediaManager: DeletedMediaManager
    private lateinit var emptyLayout: RelativeLayout
    private val TAG = "ImagesActivity"
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
        loadImages()
    }

    private fun loadImages() {
        val imageFiles: List<File> = deletedMediaManager.dirImages.listFiles()?.toList() ?: emptyList()
        Log.d(TAG, "loadImages: "+imageFiles.size)
        val imageData: List<StatusModel> = imageFiles.map { file ->
            StatusModel().apply {
                filepath = file.absolutePath
                type = "image"
            }
        }

        if (imageData.isEmpty()) {
            binding.contentRecycler.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            binding.contentRecycler.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }

        adapter = PhotoAdapter(imageData, this)
        binding.contentRecycler.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.contentRecycler.layoutManager = GridLayoutManager(this, 3) // 3 columns
    }
}