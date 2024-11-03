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

    override fun getItemCount(): Int = 7 // Number of onboarding screens

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.Companion.newInstance(
                R.drawable.one,
                "Welcome",
                "Delit: Recover deleted WhatsApp chats, images, videos, and statuses."
            )
            1 -> OnboardingFragment.Companion.newInstance(
                R.drawable.one,
                "Recover Deleted Data",
                "Retrieve deleted WhatsApp chats, media, and more."
            )
            2 -> OnboardingFragment.Companion.newInstance(
                R.drawable.one,
                "Download WhatsApp Statuses",
                "You can now download and save WhatsApp statuses."
            )
            3 -> PermissionFragment.Companion.newInstance(
                "Storage Access",
                "We need access to your device storage.",
                READ_EXTERNAL_STORAGE
            )
            4 -> PermissionFragment.Companion.newInstance(
                "Background Access",
                "Allow the app to run in the background.",
                REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            )
            5 -> PermissionFragment.Companion.newInstance(
                "Notifications",
                "Stay updated with recovery notifications.",
                POST_NOTIFICATIONS
            )
            else -> OnboardingFragment.Companion.newInstance(
                R.drawable.one,
                "You're Ready!",
                "Start recovering your deleted WhatsApp data now."
            )
        }
    }
}