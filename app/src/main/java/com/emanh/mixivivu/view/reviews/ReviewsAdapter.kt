package com.emanh.mixivivu.view.reviews

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ViewholderReviewsBinding
import com.emanh.mixivivu.model.ReviewsModel

class ReviewsAdapter(
    private val items: MutableList<ReviewsModel>
) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderReviewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context =parent.context
        val binding = ViewholderReviewsBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.starContainer.removeAllViews()

        val rating = items[position].rating

        for (i in 1..rating) {
            val star = ImageView(holder.itemView.context)
            star.layoutParams = LinearLayout.LayoutParams(20.dpToPx(), 20.dpToPx()).apply {
                marginEnd = 8.dpToPx()
            }
            star.setImageResource(R.drawable.star)
            star.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.darkYellow))
            holder.binding.starContainer.addView(star)
        }

        for (i in 1..(5 - rating)) {
            val star = ImageView(holder.itemView.context)
            star.layoutParams = LinearLayout.LayoutParams(20.dpToPx(), 20.dpToPx()).apply {
                marginEnd = 8.dpToPx()
            }
            star.setImageResource(R.drawable.star)
            star.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.binding.starContainer.addView(star)
        }

        holder.binding.name.text = items[position].name
        holder.binding.content.text = items[position].content
        holder.binding.date.text = items[position].date
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}