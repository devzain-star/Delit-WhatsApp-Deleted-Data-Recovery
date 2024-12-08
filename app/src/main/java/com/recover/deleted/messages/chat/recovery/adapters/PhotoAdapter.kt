package com.recover.deleted.messages.chat.recovery.adapters

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recover.deleted.messages.chat.recovery.databinding.StatusItemBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.ui.activities.PreviewActivity

class PhotoAdapter(val data : List<StatusModel>, val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.ImageHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageHolder {
        return ImageHolder(StatusItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ImageHolder,
        position: Int
    ) {
        val model = data[position]
        holder.binding.play.visibility = View.GONE
        Glide.with(context).load(model.filepath).override(200, 200)
            .into(holder.binding.gridImageVideo)
        Log.d("FileModel", "onBindViewHolder: "+model.filepath)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putParcelableArrayListExtra("images", data as ArrayList<out Parcelable?>?)
            intent.putExtra("position", position)
            intent.putExtra("statusdownload", "download")
            //context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ImageHolder(val binding: StatusItemBinding): RecyclerView.ViewHolder(binding.root)
}