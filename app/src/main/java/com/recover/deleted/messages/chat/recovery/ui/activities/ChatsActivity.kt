package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.ChatAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.databinding.ActivityChatsBinding
import com.recover.deleted.messages.chat.recovery.model.ContactModel
import com.recover.deleted.messages.chat.recovery.sqlite.SqliteHelper
import com.recover.deleted.messages.chat.recovery.ui.activities.ChatsActivity.onNewMessage
import com.recover.deleted.messages.chat.recovery.utils.RefreshListener
import java.lang.String
import java.util.Collections

class ChatsActivity : BaseActivity(), RefreshListener {

    private lateinit var binding: ActivityChatsBinding
    private lateinit var emptyLay: RelativeLayout
    private var sqliteHelper: SqliteHelper? = null
    var adapter: ChatAdapter? = null
    private val TAG = "ChatsActivity"

    companion object {
        var onNewMessage: onNewMessage? = null
        fun setListener(onMessage: onNewMessage?) {
            onNewMessage = onMessage
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInsets()
        init()

    }

    fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        Log.d(TAG, "init: ")
        setHeader(getString(R.string.chats))
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        sqliteHelper = SqliteHelper(activity)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.reverseLayout = true
        binding.chatRecycler.layoutManager = layoutManager

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchList() {
        Log.d(TAG, "fetchList: Called")
        var list: List<ContactModel> = ArrayList()
        list = sqliteHelper!!.getAllData()
        Collections.sort(list, CustomComparator())
        adapter = ChatAdapter(list, activity)
        adapter?.notifyDataSetChanged()
        adapter?.let {
            binding.chatRecycler.adapter = it
            if (list.isEmpty())
                emptyLay.visibility = View.VISIBLE
            else
                emptyLay.visibility = View.GONE
        }
        Log.d(TAG, "List Size: "+list.size)
    }

    override fun onRefresh(model: ContactModel?) {
        fetchList()
        Log.d(TAG, "onRefresh: ")
        if (onNewMessage != null)
            onNewMessage?.onMessageReceived(model)
    }

    interface onNewMessage {
        fun onMessageReceived(bean: ContactModel?)
    }

    class CustomComparator : Comparator<ContactModel?> {
        // may be it would be Model
        override fun compare(o1: ContactModel?, o2: ContactModel?): Int {
            return java.lang.String.valueOf(o1?.time)
                .compareTo(String.valueOf(o2?.time))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchList()
    }

}