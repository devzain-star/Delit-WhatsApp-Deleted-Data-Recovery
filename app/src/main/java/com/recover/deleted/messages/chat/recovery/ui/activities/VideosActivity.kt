package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        // Initialize DeletedMediaManager
        deletedMediaManager = DeletedMediaManager(this)

        // Load Videos
        setupRecyclerView()
        loadDeletedVideos()
    }

    private fun loadDeletedVideos() {
        val appInstallTime = getAppInstallTime()
        Log.d("AppInstallTime", "loadDeletedVideos: "+appInstallTime)
        val videosDir = File(getExternalFilesDir(null), "${getString(R.string.app_name)}/Videos")
        val videoFiles = videosDir.listFiles()?.filter { file ->
            file.isFile && file.lastModified() >= appInstallTime
        }?.sortedByDescending { it.lastModified() } ?: emptyList()

        if (videoFiles.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.GONE
            val videoModels = videoFiles.map { file ->
                StatusModel().apply {
                    filepath = file.absolutePath
                    type = "video"
                }
            }
            adapter = PhotoAdapter(videoModels, this)
            binding.contentRecycler.adapter = adapter
        }
    }


    private fun setupRecyclerView() {
        adapter = PhotoAdapter(emptyList(),this)

        binding.contentRecycler.apply {
            layoutManager = GridLayoutManager(this@VideosActivity, 3) // 3 columns
            adapter = adapter
        }
    }

    fun getAppInstallTime(): Long {
        return try {
            packageManager.getPackageInfo(packageName, 0).firstInstallTime
        } catch (e: Exception) {
            Log.e("AppInstallTime", "Error getting app install time: ${e.message}")
            System.currentTimeMillis() // Fallback to current time if install time retrieval fails
        }
    }
}