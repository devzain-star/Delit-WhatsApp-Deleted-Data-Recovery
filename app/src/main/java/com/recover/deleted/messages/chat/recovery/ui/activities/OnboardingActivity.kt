package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.OnboardingAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityOnboardingBinding

class OnboardingActivity : BaseActivity() {

    lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onboardingAdapter = OnboardingAdapter(this)
        binding.viewPager.adapter = onboardingAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->{
            tab.setIcon(R.drawable.circle_unselected)
        }}.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // Handle scrolling here if needed
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until binding.tabLayout.tabCount){
                    val tab = binding.tabLayout.getTabAt(i)
                    if(tab != null){
                        if(i == position){
                            tab.setIcon(R.drawable.circle_selected)
                        }else{
                            tab.setIcon(R.drawable.circle_unselected)
                        }
                    }
                }
            }

        })


    }

    fun nextPage() {
        val nextItem = binding.viewPager.currentItem + 1
        if (nextItem < onboardingAdapter.itemCount) {
            binding.viewPager.currentItem = nextItem
        } else {
            // Finish onboarding and move to the main activity
        }
    }

    fun homePage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}