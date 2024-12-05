package com.recover.deleted.messages.chat.recovery.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.ChatAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityChatsBinding

class ChatsActivity : BaseActivity() {

    private lateinit var binding: ActivityChatsBinding
    private lateinit var emptyLay: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.chats))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.chatRecycler.layoutManager = LinearLayoutManager(this)


    }


}