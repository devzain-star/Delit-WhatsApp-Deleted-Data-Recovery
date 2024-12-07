package com.recover.deleted.messages.chat.recovery.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.recover.deleted.messages.chat.recovery.databinding.ChatListRowBinding
import com.recover.deleted.messages.chat.recovery.model.ContactModel
import com.recover.deleted.messages.chat.recovery.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.Date

class ChatListAdapter(val data: List<ContactModel>, val context: Context) :
    RecyclerView.Adapter<ChatListAdapter.ChatHolder>() {

    var clipboard: ClipboardManager? = null
    lateinit var prefManager: PreferencesManager

    init {
        clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        prefManager = PreferencesManager(context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatHolder {
        return ChatHolder(
            ChatListRowBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(
        holder: ChatHolder,
        position: Int
    ) {
        val chat: ContactModel = data[position]

        holder.binding.tvMsg.text = chat.text
        holder.binding.tvMsgMe.text = chat.text
        holder.binding.tvMsg.text = Html.fromHtml(chat.text, Html.FROM_HTML_MODE_COMPACT)
        Linkify.addLinks(holder.binding.tvMsg, Linkify.ALL)
        holder.binding.tvMsg.movementMethod = LinkMovementMethod.getInstance()

        val formatter = SimpleDateFormat("h:mm a")
        val dateString = formatter.format(Date(chat.time))
        holder.binding.tvTime.text = dateString
        holder.binding.tvTimeMe.text = dateString

        if (chat.type.equals("me")) {
            holder.binding.chatBox.visibility = View.GONE
            holder.binding.tvTime.visibility = View.GONE
            holder.binding.chatBoxMe.visibility = View.VISIBLE
            holder.binding.tvTimeMe.visibility = View.VISIBLE
        } else {
            holder.binding.chatBoxMe.visibility = View.GONE
            holder.binding.tvTimeMe.visibility = View.GONE
            holder.binding.chatBox.visibility = View.VISIBLE
            holder.binding.tvTime.visibility = View.VISIBLE
        }

        holder.binding.tvMsgMe.setOnLongClickListener(OnLongClickListener {
            val clip = ClipData.newPlainText("message", chat.text)
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "text copied", Toast.LENGTH_SHORT).show()
            true
        })

        holder.binding.tvMsg.setOnLongClickListener(OnLongClickListener {
            val clip = ClipData.newPlainText("message", chat.text)
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "text copied", Toast.LENGTH_SHORT).show()
            true
        })

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ChatHolder(val binding: ChatListRowBinding) : RecyclerView.ViewHolder(binding.root)
}