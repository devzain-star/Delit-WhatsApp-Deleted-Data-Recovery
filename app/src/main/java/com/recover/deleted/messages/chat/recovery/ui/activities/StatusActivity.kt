package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityStatusBinding

class StatusActivity : BaseActivity() {
    private lateinit var binding: ActivityStatusBinding
    private lateinit var emptyLay: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.status))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}