package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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

        binding.sendBtn.setOnClickListener {
            val phoneNumber = binding.phoneEt.text.toString()
            val message = binding.bodyEt.text.toString()

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                sendMessage(phoneNumber, message)
            } else {
                Toast.makeText(this, "Please enter both phone number and message", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun init(){
        setHeader("Quick Send")
    }

    private fun sendMessage(phoneNumber: String, message: String) {
        try {
            val formattedNumber = if (phoneNumber.startsWith("+")) phoneNumber else "+$phoneNumber"
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber&text=$message")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp not installed or other error occurred", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}