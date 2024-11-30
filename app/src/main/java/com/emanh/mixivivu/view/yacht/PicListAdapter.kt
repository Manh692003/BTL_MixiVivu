package com.emanh.mixivivu.view.yacht

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ViewholderPicListBinding

class PicListAdapter(
    private val items:MutableList<String>,
    private val picMain: ImageView
) : RecyclerView.Adapter<PicListAdapter.ViewHolder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    class ViewHolder(val binding: ViewholderPicListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderPicListBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(holder.itemView.context)
            .load(items[position])
            .into(holder.binding.imageList)

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            Glide.with(holder.itemView.context).load(items[position]).into(picMain)
        }

        if (selectedPosition == position) {
            holder.binding.picLayout.setBackgroundResource(R.drawable.bg_light_grey_selected)
        } else {
            holder.binding.picLayout.setBackgroundResource(R.drawable.bg_light_grey2)
        }
    }
}