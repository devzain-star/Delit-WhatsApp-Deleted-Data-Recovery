package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.time.LocalTime

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(){
        binding.fabBtn.setOnClickListener(this)
        binding.chats.setOnClickListener(this)
        binding.status.setOnClickListener(this)
        binding.videos.setOnClickListener(this)
        binding.images.setOnClickListener(this)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val formattedDay = dayFormat.format(calendar.time)
        binding.date.text = formattedDate
        binding.day.text = formattedDay

        // Get the current hour of the day
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        if (currentHour in 6..18) {
            binding.imageView3.setImageResource(R.drawable.sun) // Daytime (6 AM to 6 PM)
        } else {
            binding.imageView3.setImageResource(R.drawable.thin_moon) // Nighttime (6 PM to 6 AM)
        }

        // Get the current time
        val currentTime = LocalTime.now()
        val morningTime = LocalTime.of(6, 0) // 6 AM
        val eveningTime = LocalTime.of(18, 0) // 6 PM

        // Set icon based on the time of day
        if (currentTime.isAfter(morningTime) && currentTime.isBefore(eveningTime)) {
            binding.imageView3.setImageResource(R.drawable.sun) // Daytime
        } else {
            binding.imageView3.setImageResource(R.drawable.thin_moon) // Nighttime
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabBtn -> screens.showCustomScreen(QuickSendActivity::class.java)
            R.id.chats -> screens.showCustomScreen(ChatsActivity::class.java)
            R.id.status -> screens.showCustomScreen(StatusActivity::class.java)
            R.id.videos -> screens.showCustomScreen(VideosActivity::class.java)
            R.id.images -> screens.showCustomScreen(ImagesActivity::class.java)

        }
    }
}