package com.emanh.mixivivu.view.detailYacht

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emanh.mixivivu.databinding.ViewholderHighlightsBinding

class HighlightsAdapter(
    private val items: MutableList<String>
) : RecyclerView.Adapter<HighlightsAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderHighlightsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderHighlightsBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.highlightsContent.text = items[position]
    }
}