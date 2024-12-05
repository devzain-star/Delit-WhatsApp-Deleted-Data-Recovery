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
import com.recover.deleted.messages.chat.recovery.models.ChatMessage
import com.recover.deleted.messages.chat.recovery.viewModel.ChatViewModel

class ChatsActivity : BaseActivity() {

    private lateinit var binding: ActivityChatsBinding
    private lateinit var emptyLay: RelativeLayout
    private val viewModel: ChatViewModel by viewModels()
    private val adapter = ChatAdapter()

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
        binding.chatRecycler.adapter = adapter

        viewModel.messages.observe(this, Observer { messages ->
            adapter.submitList(messages)
        })

        val filter = IntentFilter("WhatsAppMessage")
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, filter)

    }

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getParcelableExtra<ChatMessage>("message")
            message?.let { viewModel.addMessage(it) }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        super.onDestroy()
    }
}