package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityQuickSendBinding

class QuickSendActivity : BaseActivity() {
    private lateinit var binding: ActivityQuickSendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityQuickSendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

    }

    fun init(){
        setHeader("Quick Send")

    }
}