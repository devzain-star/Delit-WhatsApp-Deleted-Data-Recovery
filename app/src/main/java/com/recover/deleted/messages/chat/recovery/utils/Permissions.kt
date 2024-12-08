package com.recover.deleted.messages.chat.recovery.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class Permissions(private var context: Context) {

    fun isStorageAllow(): Boolean
    {
        for (permissions in Constants.STORAGE_PERMISSIONS)
        {
            if (ActivityCompat.checkSelfPermission(context, permissions) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }
    fun isFolderAllow() : Boolean{
        return context.contentResolver.persistedUriPermissions.size <= 0
    }
}