package com.recover.deleted.messages.chat.recovery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recover.deleted.messages.chat.recovery.databinding.ChatItemBinding
import com.recover.deleted.messages.chat.recovery.models.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val chatMessages = mutableListOf<ChatMessage>()

    fun submitList(messages: List<ChatMessage>) {
        chatMessages.clear()
        chatMessages.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatMessages[position])
    }

    override fun getItemCount(): Int = chatMessages.size

    class ChatViewHolder(private val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.tvName.text = message.name
            binding.tvMsg.text = message.text
            binding.tvTime.text = formatTime(message.time)

            message.icon?.loadDrawable(binding.root.context)?.let {
                binding.userProfile.setImageDrawable(it)
            }
        }

        private fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}