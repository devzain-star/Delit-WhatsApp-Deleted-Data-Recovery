package com.recover.deleted.messages.chat.recovery.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.recover.deleted.messages.chat.recovery.databinding.FragmentOnboardingBinding
import com.recover.deleted.messages.chat.recovery.ui.activities.OnboardingActivity



class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(imageResId: Int, title: String, description: String):OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt("imageResId", imageResId)
            args.putString("title", title)
            args.putString("description", description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root

        val imageResId = arguments?.getInt("imageResId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")

        binding.titleTextView.text = title
        binding.descriptionTextView.text = description
        imageResId?.let { binding.imageView.setImageResource(it) }

        binding.nextButton.setOnClickListener {
            (activity as? OnboardingActivity)?.nextPage()
        }
        return view
    }
}