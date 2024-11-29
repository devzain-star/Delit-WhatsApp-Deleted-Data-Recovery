package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivitySplashBinding
import com.recover.deleted.messages.chat.recovery.services.NotificationForegroundService

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {

            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        splashScreen.setKeepOnScreenCondition {
            false
        }

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeApp()
    }

    private fun initializeApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity()
        }, 2000)
    }

    private fun startMainActivity() {

        if (prefManager.isFirstTime()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else{
            //startService(Intent(this, NotificationForegroundService::class.java))
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }


}