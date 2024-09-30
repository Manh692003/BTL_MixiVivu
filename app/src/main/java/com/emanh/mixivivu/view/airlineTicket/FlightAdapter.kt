package com.emanh.mixivivu.view.airlineTicket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ViewholderFlightBinding
import com.emanh.mixivivu.model.AirlineTicketModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalTime
import java.util.Locale
import kotlin.math.roundToInt

class FlightAdapter(
    private val items: MutableList<AirlineTicketModel>,
    private val intent: Intent
) : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {

    private var context: Context? = null

    private lateinit var dialog: BottomSheetDialog

    class ViewHolder(val binding: ViewholderFlightBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderFlightBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(items[position].picLogo)
            .into(holder.binding.picAirline)

        holder.binding.nameAirline.text = items[position].airline
        holder.binding.namePlane.text = items[position].namePlane
        holder.binding.airport.text = "${items[position].startingPoint} - ${items[position].destination}"
        holder.binding.timeline.text = "${items[position].takeOffTime} - ${items[position].landingTime}"
        holder.binding.price.text = "${formatPrice(items[position].ticketPrice)} VNĐ"

        holder.itemView.setOnClickListener {
            detailAirlineTicket(holder, position)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun detailAirlineTicket(holder: ViewHolder, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_airline_ticket, null)
        dialog = BottomSheetDialog(holder.itemView.context, R.style.DialogTheme)
        dialog.setContentView(dialogView)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true

        val picAirline = dialogView.findViewById<ShapeableImageView>(R.id.picAirline)
        val airline = dialogView.findViewById<TextView>(R.id.airline)
        val namePlane = dialogView.findViewById<TextView>(R.id.namePlane)
        val seatClass = dialogView.findViewById<TextView>(R.id.seatClass)
        val luggage = dialogView.findViewById<TextView>(R.id.luggage)
        val flight = dialogView.findViewById<TextView>(R.id.flight)
        val takeOffTime = dialogView.findViewById<TextView>(R.id.takeOffTime)
        val timeline = dialogView.findViewById<TextView>(R.id.timeline)
        val landingTime = dialogView.findViewById<TextView>(R.id.landingTime)
        val sumAdult = dialogView.findViewById<TextView>(R.id.sumAdult)
        val sumChildren = dialogView.findViewById<TextView>(R.id.sumChildren)
        val sumBaby = dialogView.findViewById<TextView>(R.id.sumBaby)
        val sumPrice = dialogView.findViewById<TextView>(R.id.sumPrice)

        val dateGo = intent.getStringExtra("DATE-GO")
        val adult = intent.getStringExtra("ADULT")?.toIntOrNull() ?: 0
        val children = intent.getStringExtra("CHILDREN")?.toIntOrNull() ?: 0
        val baby = intent.getStringExtra("BABY")?.toIntOrNull() ?: 0

        Glide.with(holder.itemView.context)
            .load(items[position].picLogo)
            .into(picAirline)

        airline.text = "Hãng: ${items[position].airline}"
        namePlane.text = "Chuyến bay: ${items[position].namePlane}"
        seatClass.text = "Chỗ: ${items[position].seatClass}"
        luggage.text = "Hành lý: ${items[position].luggage}"
        flight.text = "Từ ${items[position].startingPoint} đến ${items[position].destination}"
        takeOffTime.text = "$dateGo ${items[position].takeOffTime}"
        landingTime.text = "$dateGo ${items[position].landingTime}"

        val takeOff = LocalTime.parse(items[position].takeOffTime)
        val landing = LocalTime.parse(items[position].landingTime)
        timeline.text = "Thời gian: ${Duration.between(takeOff, landing).toMinutes()} phút"

        val sumPriceAdult = adult * (items[position].ticketPrice)
        val sumPriceChildren = (children * (items[position].ticketPrice) * 0.9).roundToInt()
        val sumPriceBaby = (baby * (items[position].ticketPrice) * 0.1).roundToInt()

        sumAdult.text = "$adult người lớn: ${formatPrice(sumPriceAdult)} VNĐ"
        sumChildren.text = "$children trẻ em (giảm 10%): ${formatPrice(sumPriceChildren)} VNĐ"
        sumBaby.text = "$baby em bé (giảm 90%): ${formatPrice(sumPriceBaby)} VNĐ"
        sumPrice.text = "Tổng: ${formatPrice(sumPriceAdult + sumPriceChildren + sumPriceBaby)} VNĐ"

        dialog.show()
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }
}