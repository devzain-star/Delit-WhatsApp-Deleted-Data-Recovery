package com.recover.deleted.messages.chat.recovery.ui.fragments

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.databinding.FragmentPermissionBinding
import com.recover.deleted.messages.chat.recovery.ui.activities.OnboardingActivity

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionType: String
    private val TAG = "PermissionFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)

        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        permissionType = arguments?.getString("permissionType") ?: ""

        binding.titleTextView.text = title
        binding.descriptionTextView.text = description


        binding.nextButton.setOnClickListener {
            (activity as? OnboardingActivity)?.nextPage()
            requestPermission(permissionType)
            Log.d(TAG, "Clicked next button with permission: "+permissionType)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAndRequestPermissions() {
        Log.d(TAG, "checkAndRequestPermissions: Called")
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // For API 33+ (Tiramisu)
                requestStoragePermission()
                requestNotificationPermission()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // For API 30 to API 32 (Android 11 to 12)
                requestStoragePermission()  // External storage access
            }
            else -> {
                // For API 24 to API 29
                requestStoragePermission()
            }
        }
    }

    private fun requestPermission(permissionType: String) {
        when (permissionType) {
            "storage" -> checkAndRequestPermissions()
            "background" -> requestBackgroundPermission()
            "notification" -> requestNotificationPermission()
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 33+ request media access permissions
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(), READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(), READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        READ_MEDIA_IMAGES,
                        READ_MEDIA_VIDEO,
                        READ_MEDIA_AUDIO
                    ),
                    100
                )
            }
        } else {
            // API 24 to API 32
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                    ),
                    100
                )
            }
        }
    }

    private fun requestBackgroundPermission() {
        Log.d(TAG, "requestBackgroundPermission: Called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivityForResult(intent, 101)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun requestNotificationPermission() {
        Log.d(TAG, "requestNotificationPermission: Called")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if notification permission is already granted
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                // If not granted, guide the user to settings
                promptForNotificationAccess()
            }
        }

    }

    private fun promptForNotificationAccess() {
        // Ask user to enable notification access in settings
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    companion object {
        fun newInstance(title: String, description: String, permissionType: String): PermissionFragment {
            val fragment = PermissionFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("description", description)
            args.putString("permissionType", permissionType)
            fragment.arguments = args
            return fragment
        }
    }
}