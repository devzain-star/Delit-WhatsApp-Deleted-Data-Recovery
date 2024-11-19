package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivitySplashBinding
import com.recover.deleted.messages.chat.recovery.services.DataService
import com.recover.deleted.messages.chat.recovery.services.NotificationForegroundService

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    private var isAppReady = false
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition{ !isAppReady }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeApp()
    }

    private fun initializeApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            isAppReady = true
            startMainActivity()
        }, 2000)
    }

    private fun startMainActivity() {

        if (prefManager.isFirstTime()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else{
           // startDataService()
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    private fun startDataService(){
        startService(Intent(this, NotificationForegroundService::class.java))
        val serviceIntent = Intent(this, DataService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        }else{
            startService(serviceIntent)
        }
    }
}