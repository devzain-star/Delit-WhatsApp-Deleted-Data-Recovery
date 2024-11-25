package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.time.LocalTime
import java.time.format.DateTimeFormatter


const val REQUEST_CODE_UPDATE = 1001
class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Analytics
        analytics = Firebase.analytics
        init()
        checkForAppUpdate(this)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun init(){
        binding.fabBtn.setOnClickListener(this)
        binding.chats.setOnClickListener(this)
        binding.status.setOnClickListener(this)
        binding.videos.setOnClickListener(this)
        binding.images.setOnClickListener(this)

        // Get the current date
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val currentHour = currentTime.hour

        // Format the day and date
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())

        val dayText = currentDate.format(dayFormatter) // e.g., "Tuesday"
        val dateText = currentDate.format(dateFormatter) // e.g., "November 6"
        val yearText = currentDate.format(yearFormatter) // e.g., "2024"

        // Set the text
        binding.day.text = dayText
        binding.date.text = "$dateText\n$yearText"

        // Determine the appropriate greeting message
        val greeting = when (currentHour) {
            in 5..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 18..21 -> "Good Evening"
            else -> "Good Night"
        }
        binding.greetingText.text = greeting

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabBtn -> screens.showCustomScreen(QuickSendActivity::class.java)
            R.id.chats -> screens.showCustomScreen(ChatsActivity::class.java)
            R.id.status -> screens.showCustomScreen(StatusActivity::class.java)
            R.id.videos -> screens.showCustomScreen(VideosActivity::class.java)
            R.id.images -> screens.showCustomScreen(ImagesActivity::class.java)

        }
    }

    fun checkForAppUpdate(activity: Activity) {
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}