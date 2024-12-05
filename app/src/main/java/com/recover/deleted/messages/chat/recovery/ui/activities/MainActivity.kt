package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository
import com.recover.deleted.messages.chat.recovery.databinding.ActivityMainBinding
import com.recover.deleted.messages.chat.recovery.utils.Constants.DELIT_PREFS
import com.recover.deleted.messages.chat.recovery.utils.Constants.DELIT_STATUS_PREFS
import com.recover.deleted.messages.chat.recovery.utils.Constants.REQUEST_CODE_UPDATE
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModel
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModelFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding
    private lateinit var statusViewModel: StatusViewModel

    private val folderPickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                saveUriToPreferences(uri)
                loadStatuses(uri)
            } else {
                screens.showToast("Folder selection canceled")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        analytics = Firebase.analytics
        init()
        checkForAppUpdate()
        setupStatuses()

        if (!isNotificationAccessEnabled(this)) {
            showNotificationAccessDialog()
        }

    }

    override fun onResume() {
        super.onResume()
        setupStatuses()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        binding.apply {
            fabBtn.setOnClickListener(this@MainActivity)
            chats.setOnClickListener(this@MainActivity)
            status.setOnClickListener(this@MainActivity)
            videos.setOnClickListener(this@MainActivity)
            images.setOnClickListener(this@MainActivity)
        }

        setGreetingAndDate()
        val repository = WhatsAppStatusRepository(applicationContext)
        val viewModelFactory = StatusViewModelFactory(repository)
        statusViewModel = ViewModelProvider(this, viewModelFactory)[StatusViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setGreetingAndDate() {
        val currentDate = LocalDate.now()
        val currentHour = LocalTime.now().hour

        binding.apply {
            day.text = currentDate.format(DateTimeFormatter.ofPattern("EEEE", Locale.getDefault()))
            date.text = currentDate.format(DateTimeFormatter.ofPattern("MMMM d\nyyyy", Locale.getDefault()))
            greetingText.text = when (currentHour) {
                in 5..11 -> "Good Morning"
                in 12..17 -> "Good Afternoon"
                in 18..21 -> "Good Evening"
                else -> "Good Night"
            }
        }
    }

    private fun setupStatuses() {
        val savedUri = getSavedUri()
        if (savedUri != null) {
            loadStatuses(savedUri)
        } else {
            //openFolderPicker()
        }
    }

    private fun loadStatuses(uri: Uri) {
        statusViewModel.getStatusesFromUri(uri).observe(this) { statuses ->
            binding.available.text = statuses.size.toString()
        }
    }

    private fun checkForAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    private fun openFolderPicker() {
        folderPickerLauncher.launch(null)
    }

    private fun saveUriToPreferences(uri: Uri) {
        getSharedPreferences(DELIT_PREFS, MODE_PRIVATE).edit()
            .putString(DELIT_STATUS_PREFS, uri.toString())
            .apply()
    }

    private fun getSavedUri(): Uri? {
        val uriString = getSharedPreferences(DELIT_PREFS, MODE_PRIVATE)
            .getString(DELIT_STATUS_PREFS, null)
        return uriString?.let { Uri.parse(it) }
    }



    override fun onClick(view: View) {
        val activity = when (view.id) {
            R.id.fabBtn -> QuickSendActivity::class.java
            R.id.chats -> ChatsActivity::class.java
            R.id.status -> StatusActivity::class.java
            R.id.videos -> VideosActivity::class.java
            R.id.images -> ImagesActivity::class.java
            else -> null
        }
        activity?.let { screens.showCustomScreen(it) }
    }

    private fun isNotificationAccessEnabled(context: Context): Boolean {
        val enabledListeners =
            android.provider.Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return enabledListeners?.contains(context.packageName) == true
    }

    private fun showNotificationAccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Access Required")
            .setMessage("Please enable notification access to detect WhatsApp messages.")
            .setPositiveButton("Grant Access") { _, _ ->
                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
