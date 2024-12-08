package com.recover.deleted.messages.chat.recovery.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.utils.Permissions
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import com.recover.deleted.messages.chat.recovery.utils.Screens

open class BaseActivity: AppCompatActivity() {
    lateinit var activity: Activity
    lateinit var screens: Screens
    lateinit var prefManager: PreferencesManager
    lateinit var permissions: Permissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = this
        screens = Screens(activity)
        prefManager = PreferencesManager(activity)
        permissions = Permissions(this)

    }

    fun setHeader(title: String) {
        try {
            findViewById<TextView>(R.id.title).text = title
            findViewById<ImageView>(R.id.back).setOnClickListener(View.OnClickListener { onBackPressed() })
        } catch (_: Exception) {
        }
    }

}