package com.recover.deleted.messages.chat.recovery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recover.deleted.messages.chat.recovery.databinding.StatusItemBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.utils.Utils
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.ui.activities.PreviewActivity

class StatusAdapter(private val statusesList: List<StatusModel>, private val context: Context) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatusViewHolder {
        return StatusViewHolder(StatusItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statusesList[position]
        holder.bind(status)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra("status", status)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = statusesList.size

    inner class StatusViewHolder(
        val binding: StatusItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(status: StatusModel) {
             if (Utils.isVideoFile(status.filepath)) {
                binding.play.visibility = View.VISIBLE
            } else {
                binding.play.visibility = View.GONE
            }

            Glide.with(binding.root.context)
                .load(status.filepath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.gridImageVideo)

            binding.root.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "Clicked on: ${status.type}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}