package com.recover.deleted.messages.chat.recovery.adapters

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.fragment.app.Fragment
import com.recover.deleted.messages.chat.recovery.R
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.recover.deleted.messages.chat.recovery.ui.fragments.OnboardingFragment
import com.recover.deleted.messages.chat.recovery.ui.fragments.PermissionFragment


class OnboardingAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                R.drawable.one,
                "Welcome 👋🏼",
                "Recover deleted chats, images, videos, and statuses."
            )
            1 -> OnboardingFragment.newInstance(
                R.drawable.recover,
                "Recover Data",
                "Retrieve deleted chats, media, and more."
            )
            2 -> OnboardingFragment.newInstance(
                R.drawable.status,
                "Download Status",
                "You can now download and save statuses."
            )
            3 -> PermissionFragment.newInstance(
                R.drawable.storage,
                "Access Storage",
                "We need access to your device storage.",
                "storage"
            )
            4 -> PermissionFragment.newInstance(
                R.drawable.background,
                "Background Access",
                "Allow the app to run in the background.",
                "background"
            )
            5 -> PermissionFragment.newInstance(
                R.drawable.allow_notification,
                "Allow Notifications",
                "Stay updated with recovery notifications.",
                "notification"
            )
            else -> OnboardingFragment.newInstance(
                R.drawable.one,
                "You're Ready!",
                "Start recovering your deleted data now."
            )
        }
    }
}