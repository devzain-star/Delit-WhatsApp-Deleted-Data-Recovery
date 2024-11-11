package com.recover.deleted.messages.chat.recovery.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import com.recover.deleted.messages.chat.recovery.utils.Screens

open class BaseActivity: AppCompatActivity() {
    lateinit var activity: Activity
    lateinit var screens: Screens
    lateinit var prefManager: PreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = this
        screens = Screens(activity)
        prefManager = PreferencesManager(activity)

    }
}