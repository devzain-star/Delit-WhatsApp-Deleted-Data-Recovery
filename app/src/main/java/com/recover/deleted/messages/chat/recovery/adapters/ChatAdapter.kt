package com.recover.deleted.messages.chat.recovery.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.databinding.ChatItemBinding
import com.recover.deleted.messages.chat.recovery.model.ContactModel
import com.recover.deleted.messages.chat.recovery.services.NotificationService
import com.recover.deleted.messages.chat.recovery.sqlite.SqliteHelper
import com.recover.deleted.messages.chat.recovery.ui.activities.ChatViewActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.toString


class ChatAdapter(val data: List<ContactModel>, val context: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val TAG = "ChatAdapter"
    private var sqliteHelper: SqliteHelper? = null

    init {
        sqliteHelper = SqliteHelper(context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatAdapter.ChatHolder {
        return ChatHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatAdapter.ChatHolder, position: Int) {
        val item = data[position]
        holder.binding.tvName.text = item.name
        holder.binding.tvMsg.text = item.text

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NotificationService.notificationModels.let {
                    if (it != null) {
                        for (notification in it) {
                            if (notification.name.equals(item.name)) {
                                 Glide.with(context).load(notification.icon).placeholder(context.getDrawable(R.drawable.avatar)).into(holder.binding.userProfile);
                               //holder.binding.userProfile.setImageIcon(notification.icon)
                                break
                            }
                        }
                    }
                }
            }else holder.binding.userProfile.setImageURI(
                Uri.parse(item.logo)
            )
        }catch (e: Exception){
            Log.d(TAG, "onBindViewHolder: "+e.message)
            Glide.with(context).load(context.getDrawable(R.drawable.avatar)).into(holder.binding.userProfile);
            //holder.binding.userProfile.setImageDrawable(context.getDrawable(R.drawable.avatar))
        }

        val id = sqliteHelper!!.getCountsDataByID(item.id)
        if (id == 0) {
            holder.binding.tvCounter.visibility = View.INVISIBLE
        } else {
            holder.binding.tvCounter.visibility = View.VISIBLE
            holder.binding.tvCounter.text = id.toString() + " messages"
        }

        val formatter = SimpleDateFormat("h:mm a")
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        val dateString = formatter.format(Date(item.time))
        if (getDate() == dateFormatter.format(Date(item.time)))
            holder.binding.tvTime.text = dateString
        else {
            holder.binding.tvTime.text = dateFormatter.format(Date(item.time))
        }

        holder.binding.rootChatItem.setOnClickListener(View.OnClickListener {
            context
                .startActivity(
                    Intent(context, ChatViewActivity::class.java)
                        .putExtra("cid", item.id)
                        .putExtra("name", item.name)
                        .putExtra("logo", item.logo)
                )
        })


    }


    override fun getItemCount(): Int {
        return data.size
    }

    class ChatHolder(val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getDate(): String? {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        return dateFormatter.format(Date(Calendar.getInstance().timeInMillis))
    }
}
