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
import com.recover.deleted.messages.chat.recovery.databinding.ActivityVideosBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import java.io.File

class VideosActivity : BaseActivity() {
    private lateinit var binding: ActivityVideosBinding
    private lateinit var emptyLayout: RelativeLayout
    private lateinit var adapter: PhotoAdapter
    private lateinit var deletedMediaManager: DeletedMediaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emptyLayout = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.videos))

        deletedMediaManager = DeletedMediaManager(this)

        setupRecyclerView()
        loadDeletedVideos()
    }

    private fun loadDeletedVideos() {
        val videoFiles: List<File> = deletedMediaManager.dirVideos.listFiles()?.toList() ?: emptyList()

        val videoData: List<StatusModel> = videoFiles.map { file ->
            StatusModel().apply {
                filepath = file.absolutePath
                type = "video"
            }
        }

        if (videoFiles.isEmpty()) {
            binding.contentRecycler.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            binding.contentRecycler.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
        adapter = PhotoAdapter(videoData, this)
        binding.contentRecycler.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.contentRecycler.layoutManager = GridLayoutManager(this, 3) // 3 columns
    }
}