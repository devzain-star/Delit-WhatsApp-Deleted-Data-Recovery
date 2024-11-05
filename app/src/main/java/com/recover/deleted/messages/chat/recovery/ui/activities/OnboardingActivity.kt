package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
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
            tab.customView = createTabDot(binding.tabLayout, position == 0)
        }}.attach()

        for (i in 0 until binding.tabLayout.tabCount){
            val tab = binding.tabLayout.getTabAt(i)
            if (tab != null && tab.icon != null) {
                tab.view.setPadding(10, 0, 10, 0)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setUpTabDots(binding.tabLayout, position)
            }
        })

    }

    private fun createTabDot(tabLayout: TabLayout, isSelected: Boolean): View {
        val tabDot = LayoutInflater.from(this).inflate(R.layout.tab_dot, tabLayout, false) as ImageView
        tabDot.setImageResource(if (isSelected) R.drawable.circle_selected else R.drawable.circle_unselected)
        return tabDot
    }

    private fun setUpTabDots(tabLayout: TabLayout, selectedPosition: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null && tab.customView is ImageView) {
                val tabDot = tab.customView as ImageView
                tabDot.setImageResource(if (i == selectedPosition) R.drawable.circle_selected else R.drawable.circle_unselected)
            }
        }
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