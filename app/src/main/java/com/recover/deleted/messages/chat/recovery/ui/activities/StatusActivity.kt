package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.StatusAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository
import com.recover.deleted.messages.chat.recovery.databinding.ActivityStatusBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.Constants.DELIT_PREFS
import com.recover.deleted.messages.chat.recovery.utils.Constants.DELIT_STATUS_PREFS
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModel
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModelFactory

class StatusActivity : BaseActivity() {
    private lateinit var binding: ActivityStatusBinding
    private lateinit var emptyLay: RelativeLayout
    private lateinit var statusAdapter: StatusAdapter

    private val viewModel: StatusViewModel by viewModels {
        StatusViewModelFactory(WhatsAppStatusRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.status))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeStatuses()
    }

    private fun setupRecyclerView() {
        statusAdapter = StatusAdapter(emptyList(),this)

        binding.statusRecycler.apply {
            layoutManager = GridLayoutManager(this@StatusActivity, 3) // 3 columns
            adapter = statusAdapter
        }
    }

    private fun observeStatuses() {
        val uri = getSavedUri()
        if (uri != null) {
            viewModel.getStatusesFromUri(uri).observe(this) { statuses ->
                updateStatuses(statuses)
            }
        } else {
            Toast.makeText(this, "Please select the WhatsApp Status folder.", Toast.LENGTH_SHORT).show()
            showHelpDialog()
        }
    }

    private fun updateStatuses(statuses: List<StatusModel>) {
        Log.d("statusList", "updateStatuses: "+statuses.size)
         statuses.forEach { status ->
            Log.d("statusList", "Status: ${status.filepath}")
            Log.d("statusList", "Status: ${status.type}")
        }

        statusAdapter = StatusAdapter(statuses, this)
        binding.statusRecycler.adapter = statusAdapter
        if (statuses.isEmpty()) {
            emptyLay.visibility = RelativeLayout.VISIBLE
        } else {
            emptyLay.visibility = RelativeLayout.GONE
        }
    }

    private fun openFolderPicker() {
        folderPickerLauncher.launch(null)
    }


    private val folderPickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            if (uri != null) {
                // Persist permissions for the selected folder
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                saveUriToPreferences(uri)
                viewModel.getStatusesFromUri(uri).observe(this) { statuses ->
                    updateStatuses(statuses)
                }
            } else {
                Toast.makeText(this, "Folder selection canceled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveUriToPreferences(uri: Uri) {
        val prefs = getSharedPreferences(DELIT_PREFS, MODE_PRIVATE)
        prefs.edit().putString(DELIT_STATUS_PREFS, uri.toString()).apply()
    }

    private fun getSavedUri(): Uri? {
        val prefs = getSharedPreferences(DELIT_PREFS, MODE_PRIVATE)
        val uriString = prefs.getString(DELIT_STATUS_PREFS, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }

    private fun showHelpDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.apply {
            setTitle("Grant Access to Status Folder")
            setMessage(
                """To recover WhatsApp status, we need access to the .status folder. Please follow the steps:
                1. Select the "Allow Access" button below.
                2. Choose the 'WhatsApp' folder from your internal storage.
                3. Grant permission for our app to read and write to this folder.
                """
            )
            setIcon(R.drawable.info)
            setPositiveButton("Allow Access") { _, _ ->
                openFolderPicker()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                screens.showToast("Access Denied")
                dialog.dismiss()
            }
        }
        dialogBuilder.create().show()
    }

}