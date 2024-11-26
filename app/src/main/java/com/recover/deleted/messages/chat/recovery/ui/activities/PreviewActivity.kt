package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.databinding.ActivityPreviewBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.Utils
import java.io.File
import java.io.IOException
import android.os.Environment
import android.content.Context
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import com.recover.deleted.messages.chat.recovery.base.BaseActivity

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private lateinit var status: StatusModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        status = intent.getParcelableExtra("status") ?: return

        if (Utils.isImageFile(status.filepath)) {
            binding.imageView.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            Glide.with(this)
                .load(status.filepath)
                .placeholder(R.drawable.placeholder)
                .into(binding.imageView)
        } else if (Utils.isVideoFile(status.filepath)) {
            binding.imageView.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
            binding.videoView.setVideoURI(Uri.parse(status.filepath))
            binding.videoView.setOnPreparedListener {
                binding.videoView.start()
            }
        }

        binding.downloadIV.setOnClickListener { downloadStatus() }
        binding.shareIV.setOnClickListener { shareStatus() }

    }


    private fun downloadStatus() {
        val statusUri = Uri.parse(status.filepath) // Parse the content URI
        if (statusUri == null) {
              screens.showToast("Unable to download.")
            return
        }
        val fileName = getFileNameFromUri(statusUri) ?: "status_${System.currentTimeMillis()}.file"
        val targetDir = File(getExternalFilesDir(null), "${getString(R.string.app_name)}/Status/")
        if (!targetDir.exists()) targetDir.mkdirs()

        val targetFile = File(targetDir, fileName)

        try {
            contentResolver.openInputStream(statusUri)?.use { inputStream ->
                targetFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Optionally add to MediaStore to make the file visible in the gallery
            addToMediaStore(targetFile)
            screens.showToast("Status Downloaded")
        } catch (e: Exception) {
            Log.e("PreviewActivity", "Error downloading status: ${e.message}")
            screens.showToast("Error downloading status")
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    if (!name.isNullOrEmpty()) return name
                }
            }
            uri.lastPathSegment?.substringAfterLast('/') ?: "unknown_file"
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PreviewActivity", "Failed to retrieve file name: ${e.message}")
            null
        }
    }

    private fun addToMediaStore(file: File) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, Utils.getMimeType(file))
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${getString(R.string.app_name)}/Status")
        }

        try {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    file.inputStream().copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PreviewActivity", "Error adding to MediaStore: ${e.message}")
        }
    }


    private fun shareStatus() {
        val statusUri = Uri.parse(status.filepath) // Parse the content URI
        if (statusUri == null) {
            Toast.makeText(this, "Status URI is null. Unable to share.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Use content URI directly for sharing
            val mimeType = contentResolver.getType(statusUri) ?: "application/octet-stream"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, statusUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary permission
            }

            // Start the sharing intent
            startActivity(Intent.createChooser(shareIntent, "Share Status Via"))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PreviewActivity", "Error sharing status: ${e.message}")
            Toast.makeText(this, "Error sharing status: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}
