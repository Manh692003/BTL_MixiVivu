package com.emanh.mixivivu.view.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emanh.mixivivu.databinding.ViewholderFeaturesBinding

class FeaturesAdapter(
    private val items: MutableList<String>
) : RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderFeaturesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderFeaturesBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.feature.text = items[position]
    }
}