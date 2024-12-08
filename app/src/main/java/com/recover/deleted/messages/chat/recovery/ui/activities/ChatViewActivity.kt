package com.recover.deleted.messages.chat.recovery.ui.activities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.ChatListAdapter
import com.recover.deleted.messages.chat.recovery.databinding.ActivityChatViewBinding
import com.recover.deleted.messages.chat.recovery.model.ContactModel
import com.recover.deleted.messages.chat.recovery.services.NotificationService
import com.recover.deleted.messages.chat.recovery.sqlite.SqliteHelper

class ChatViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatViewBinding
    var sqliteHelper: SqliteHelper? = null
    var list: MutableList<ContactModel>? = null
    var cid = 0
    var notification: Notification? = null

    var adapter: ChatListAdapter? = null
    var name: String? = null
    var logo: String? = null
    var isVisible: Boolean? = false
    private val TAG = "ChatViewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInsets()
        init()

        val intent = intent

        if (intent != null) {
            cid = intent.getIntExtra("cid", 0)
            name = intent.getStringExtra("name")
            logo = intent.getStringExtra("logo")

            try {
                logo = intent.getStringExtra("logo")
                if (logo != null) binding.userPic.setImageURI(Uri.parse(logo))
            } catch (_: Exception) {
            }

            //search in list
            for (model in NotificationService.notificationModels!!) {
                if (model.name.equals(name)) {
                    notification = model.notification
                    binding.bottomLayout.visibility = View.VISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        binding.userPic.setImageIcon(model.icon)
                    break
                }
            }

            list = sqliteHelper!!.getData(cid)
            sqliteHelper!!.resetCount(cid, name!!)
            if (name != null) {
                binding.tvName.text = name
            } else binding.tvName.text = getString(R.string.app_name)
        }else Log.d(TAG, "empty: ")

        if (list!!.isNotEmpty()) {
            val layoutManager = LinearLayoutManager(applicationContext)
            binding.rvChat.layoutManager = layoutManager
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager.isSmoothScrollbarEnabled = true
            layoutManager.stackFromEnd = true
            adapter = ChatListAdapter(list!!, applicationContext)
            binding.rvChat.adapter = adapter
            binding.rvChat.layoutManager?.scrollToPosition(list!!.size - 1)
            Log.d(TAG, "Chats: " + Gson().toJson(list))

        }

        binding.back.setOnClickListener {
            finish()
        }

        ChatsActivity.setListener(object : ChatsActivity.onNewMessage {
            @SuppressLint("NotifyDataSetChanged")
            override fun onMessageReceived(chat: ContactModel?) {
                if (chat?.name.equals(name)) {
                    chat?.time = System.currentTimeMillis()
                    chat?.type = "other"
                    chat?.let { list!!.add(it) }
                    if (isVisible == true)
                        sqliteHelper!!.resetCount(cid, name!!)

                    adapter!!.notifyDataSetChanged()
                    binding.rvChat.layoutManager?.scrollToPosition(list!!.size - 1)
                }
            }
        })

        binding.send.setOnClickListener {
            if (binding.message.text.toString().trim { it <= ' ' }.isEmpty()) {
                Log.d(TAG, "null: ")
                return@setOnClickListener
            }

            if (notification != null) {
                val chat = ContactModel()
                chat.text = binding.message.text.toString().trim { it <= ' ' }
                chat.time = System.currentTimeMillis()
                chat.type = "me"
                list?.add(chat)
                adapter!!.notifyDataSetChanged()

                sqliteHelper!!.addContactID(
                    ContactModel(
                        "",
                        name,
                        logo,
                        binding.message.text.toString().trim { it <= ' ' },
                        System.currentTimeMillis(),
                        "me"
                    )
                )
                binding.rvChat.layoutManager?.scrollToPosition(list!!.size - 1)
                val actions = notification!!.actions
                if (actions != null) {
                    for (act in actions) {
                        if (act != null && act.remoteInputs != null) {
                            if (act.title.toString().contains("Reply")) {
                                if (act.remoteInputs != null) {
                                    Log.d(
                                        TAG, "onClick:isSent " + sendNativeIntent(
                                            applicationContext,
                                            act,
                                            binding.message.text.toString().trim { it <= ' ' })
                                    )
                                    break
                                }
                            }
                        }
                    }
                }
                binding.message.setText("")
            }
        }

    }

    fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        sqliteHelper = SqliteHelper(this)
        list = ArrayList<ContactModel>()
    }

    private fun sendNativeIntent(
        context: Context,
        action: Notification.Action,
        msg: String
    ): Boolean {
        for (rem in action.remoteInputs) {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putCharSequence(rem.resultKey, msg)
            RemoteInput.addResultsToIntent(action.remoteInputs, intent, bundle)
            try {
                action.actionIntent.send(context, 0, intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return false
            }
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        isVisible = true
    }

    override fun onPause() {
        super.onPause()
        isVisible = false
    }

}