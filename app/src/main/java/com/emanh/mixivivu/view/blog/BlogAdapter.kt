package com.emanh.mixivivu.view.blog

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.emanh.mixivivu.databinding.ViewholderBlogBinding
import com.emanh.mixivivu.model.BlogModel

class BlogAdapter(private val items: MutableList<BlogModel>)
    : RecyclerView.Adapter<BlogAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder (val binding: ViewholderBlogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderBlogBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(items[position].picList[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picUrl)

        holder.binding.title.text = items[position].title
        holder.binding.content.text = items[position].intro
        holder.binding.date.text = items[position].date

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailBlogActivity::class.java)
            intent.putExtra("objectBlog", items[position])
            holder.itemView.context.startActivity(intent)
        }
    }
}