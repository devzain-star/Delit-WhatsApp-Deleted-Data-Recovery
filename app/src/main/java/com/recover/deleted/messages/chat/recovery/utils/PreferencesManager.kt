package com.recover.deleted.messages.chat.recovery.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


class PreferencesManager(context: Context) {
    private val PREF_NAME = "com.recover.deleted.messages.chat.recovery"
    private val KEY_IS_FIRST_TIME = "isFirstTime"

    //Data fields
    private val KEY_SERVICE_ENABLED = "pref_service_enabled"
    private val KEY_GROUP_REPLY_ENABLED = "pref_group_reply_enabled"
    private val KEY_AUTO_REPLY_THROTTLE_TIME_MS = "pref_auto_reply_throttle_time_ms"
    private val KEY_SELECTED_APPS_ARR = "pref_selected_apps_arr"
    private val KEY_IS_APPEND_WATOMATIC_ATTRIBUTION = "pref_is_append_watomatic_attribution"
    private val KEY_GITHUB_RELEASE_NOTES_ID = "pref_github_release_notes_id"
    private val KEY_PURGE_MESSAGE_LOGS_LAST_TIME = "pref_purge_message_logs_last_time"
    private val KEY_PLAY_STORE_RATING_STATUS = "pref_play_store_rating_status"
    private val KEY_PLAY_STORE_RATING_LAST_TIME = "pref_play_store_rating_last_time"
    private val KEY_SHOW_FOREGROUND_SERVICE_NOTIFICATION = "pref_show_foreground_service_notification"
    private val KEY_REPLY_CONTACTS = "pref_reply_contacts"
    private val KEY_REPLY_CONTACTS_TYPE = "pref_reply_contacts_type"
    private val KEY_REPLY_CUSTOM_NAMES = "pref_reply_custom_names"
    private val KEY_SELECTED_CONTACT_NAMES = "pref_selected_contacts_names"
    private var KEY_IS_SHOW_NOTIFICATIONS_ENABLED: String? = null
    private var KEY_SELECTED_APP_LANGUAGE: String? = null
    private val MODE = "mode"
    private val DATE = "date"
    private val IS_PERMISSION_SET = "ispermission";
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {

        val newInstall = (!sharedPreferences.contains(KEY_SERVICE_ENABLED)
                && !sharedPreferences.contains(KEY_SELECTED_APPS_ARR))
        if (newInstall) {
            setShowNotificationPref(true)
        }

        if (isFirstTime()) {
            if (!sharedPreferences.contains(KEY_IS_APPEND_WATOMATIC_ATTRIBUTION)) {
                setAppendDelitAttribution(true)
            }
        }
    }

    fun isServiceEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SERVICE_ENABLED, false)
    }

    fun setServicePref(enabled: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_SERVICE_ENABLED, enabled)
        editor.apply()
    }

    fun isGroupReplyEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_GROUP_REPLY_ENABLED, false)
    }

    fun setGroupReplyPref(enabled: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_GROUP_REPLY_ENABLED, enabled)
        editor.apply()
    }

    fun getAutoReplyDelay(): Long {
        return sharedPreferences.getLong(KEY_AUTO_REPLY_THROTTLE_TIME_MS, 0)
    }

    fun setAutoReplyDelay(delay: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong(KEY_AUTO_REPLY_THROTTLE_TIME_MS, delay)
        editor.apply()
    }


    private fun serializeAndSetEnabledPackageList(packageList: Collection<String?>): String? {
        val jsonStr = Gson().toJson(packageList)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_SELECTED_APPS_ARR, jsonStr)
        editor.apply()
        return jsonStr
    }

    private fun setAppendDelitAttribution(enabled: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_APPEND_WATOMATIC_ATTRIBUTION, enabled)
        editor.apply()
    }

    fun isAppendDelitAttributionEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_APPEND_WATOMATIC_ATTRIBUTION, false)
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME, true)
    }

    fun setFirstTime(value: Boolean) {
        editor.putBoolean(KEY_IS_FIRST_TIME, value)
        editor.apply()
    }

    fun getSelectedLanguageStr(defaultLangStr: String?): String? {
        return sharedPreferences.getString(KEY_SELECTED_APP_LANGUAGE, defaultLangStr)
    }

    fun isShowNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_SHOW_NOTIFICATIONS_ENABLED, false)
    }

    private fun setShowNotificationPref(enabled: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_SHOW_NOTIFICATIONS_ENABLED, enabled)
        editor.apply()
    }

    fun getLastPurgedTime(): Long {
        return sharedPreferences.getLong(KEY_PURGE_MESSAGE_LOGS_LAST_TIME, 0)
    }

    fun setPurgeMessageTime(purgeMessageTime: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong(KEY_PURGE_MESSAGE_LOGS_LAST_TIME, purgeMessageTime)
        editor.apply()
    }

    fun setShowForegroundServiceNotification(enabled: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_SHOW_FOREGROUND_SERVICE_NOTIFICATION, enabled)
        editor.apply()
    }

    fun isForegroundServiceNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SHOW_FOREGROUND_SERVICE_NOTIFICATION, false)
    }

    fun setReplyToNames(names: Set<String?>?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet(KEY_SELECTED_CONTACT_NAMES, names)
        editor.apply()
    }

    fun getReplyToNames(): Set<String?>? {
        return sharedPreferences.getStringSet(KEY_SELECTED_CONTACT_NAMES, HashSet<String>())
    }

    fun getCustomReplyNames(): Set<String?>? {
        return sharedPreferences.getStringSet(KEY_REPLY_CUSTOM_NAMES, HashSet<String>())
    }

    fun setCustomReplyNames(names: Set<String?>?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet(KEY_REPLY_CUSTOM_NAMES, names)
        editor.apply()
    }

    fun isContactReplyEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_REPLY_CONTACTS, false)
    }

    fun isContactReplyBlacklistMode(): Boolean? {
        return sharedPreferences.getString(KEY_REPLY_CONTACTS_TYPE, "pref_blacklist") == "pref_blacklist"
    }

    fun setDarkMode(isFirstTime: Boolean) {
        editor.putBoolean(MODE, isFirstTime)
        editor.commit()
    }

    fun getDarkMode(): Boolean {
        return sharedPreferences.getBoolean(MODE, false)
    }

    fun setDate(key: Long) {
        editor.putLong(DATE, key)
        editor.commit()
    }

    fun getDate(): Long {
        return sharedPreferences.getLong(DATE, 0)
    }

    fun setIsPermissionSet(isPermissionSet: Boolean?) {
        editor.putBoolean(IS_PERMISSION_SET, isPermissionSet!!)
        editor.commit()
    }

    fun getIsPermissionSet(): Boolean? {
        return sharedPreferences.getBoolean(IS_PERMISSION_SET, false)
    }

}