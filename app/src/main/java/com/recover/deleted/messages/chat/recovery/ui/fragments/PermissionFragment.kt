package com.recover.deleted.messages.chat.recovery.ui.fragments

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.databinding.FragmentPermissionBinding
import com.recover.deleted.messages.chat.recovery.ui.activities.OnboardingActivity

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionType: String


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

        binding.grantPermissionButton.setOnClickListener {
            requestPermission(permissionType)
        }

        binding.nextButton.setOnClickListener {
            (activity as? OnboardingActivity)?.nextPage()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAndRequestPermissions() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // For API 33+
                requestStoragePermission()
                requestNotificationPermission()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                // For API 30 to API 32
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
            "notification" -> checkAndRequestPermissions()
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 33+ request media access permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VIDEO,
                    READ_MEDIA_AUDIO
                ),
                100
            )
        } else {
            // API 24 to API 32
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

    private fun requestBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            startActivity(intent)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(POST_NOTIFICATIONS),
                101
            )
        }
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