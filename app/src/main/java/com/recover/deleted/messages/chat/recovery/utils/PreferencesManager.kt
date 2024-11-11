package com.recover.deleted.messages.chat.recovery.utils

import android.content.Context

private const val PREF_NAME = "com.recover.deleted.messages.chat.recovery"
    private const val KEY_IS_FIRST_TIME = "isFirstTime"

class PreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME, true)
    }

    fun setFirstTime(value: Boolean) {
        editor.putBoolean(KEY_IS_FIRST_TIME, value)
        editor.apply()
    }


}