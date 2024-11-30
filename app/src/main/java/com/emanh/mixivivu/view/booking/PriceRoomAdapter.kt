package com.emanh.mixivivu.view.booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ViewholderPriceRoomBinding
import com.emanh.mixivivu.helper.OnOrderShipClickListener
import com.emanh.mixivivu.model.TypeRoomModel
import com.emanh.mixivivu.view.yacht.PicListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import java.text.NumberFormat
import java.util.Locale

class PriceRoomAdapter(
    private val items:MutableList<TypeRoomModel>,
    private val listener: OnOrderShipClickListener
) : RecyclerView.Adapter<PriceRoomAdapter.ViewHolder>() {
    private var context: Context? = null
    private var quantityRooms: MutableList<Int> = MutableList(items.size) { 0 }
    private var sumPriceRooms: MutableList<Int> = MutableList(items.size) { 0 }
    private var sumPrice: Int = 0

    private lateinit var dialog: BottomSheetDialog

    class ViewHolder(val binding: ViewholderPriceRoomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderPriceRoomBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = "Phòng ${items[position].name}"
        holder.binding.area.text = "${items[position].area} m²"
        holder.binding.capacity.text = "Tối đa: ${items[position].capacity}"
        holder.binding.price.text = "${formatPrice(items[position].price)}đ / Khách"

        Glide.with(holder.itemView.context)
            .load(items[position].banner)
            .into(holder.binding.picUrl)

        holder.itemView.setOnClickListener {
            detailRoom(holder, position)
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun detailRoom(holder: ViewHolder, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detail_room, null)
        dialog = BottomSheetDialog(holder.itemView.context, R.style.DialogTheme)
        dialog.setContentView(dialogView)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true

        val picUrl = dialogView.findViewById<ShapeableImageView>(R.id.picUrl)
        val listPic = dialogView.findViewById<RecyclerView>(R.id.listPic)
        val title = dialogView.findViewById<TextView>(R.id.title)
        val area = dialogView.findViewById<TextView>(R.id.area)
        val capacity = dialogView.findViewById<TextView>(R.id.capacity)
        val listDetailRoom = dialogView.findViewById<RecyclerView>(R.id.listDetailRoom)
        val minus = dialogView.findViewById<AppCompatButton>(R.id.minus)
        val quantity = dialogView.findViewById<TextView>(R.id.quantity)
        val plus = dialogView.findViewById<AppCompatButton>(R.id.plus)
        val orderShip = dialogView.findViewById<AppCompatButton>(R.id.orderShip)

        val picList = ArrayList<String>()
        for (imageUrl in items[position].picUrl) {
            picList.add(imageUrl)
        }

        Glide.with(holder.itemView.context)
            .load(picList[0])
            .into(picUrl)

        listPic.adapter = PicListAdapter(picList, picUrl)
        listPic.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        title.text = "Phòng ${items[position].name}"
        area.text = "${items[position].area} m²"
        capacity.text = "Tối đa: ${items[position].capacity}"

        val listDetail = ArrayList<String>()
        for (feature in items[position].features) {
            listDetail.add(feature)
        }

        listDetailRoom.layoutManager = LinearLayoutManager(context)
        listDetailRoom.adapter = FeaturesAdapter(listDetail)

        updateQuantity(quantity, position)

        plus.setOnClickListener {
            if (quantityRooms[position] < 99) {
                quantityRooms[position]++
                updateQuantity(quantity, position)
            } else {
                "Số lượng phòng không được lớn hơn 99".toast()
            }
        }

        minus.setOnClickListener {
            if (quantityRooms[position] > 0) {
                quantityRooms[position]--
                updateQuantity(quantity, position)
            } else {
                "Số lượng phòng không được nhỏ hơn 0".toast()
            }
        }

        orderShip.setOnClickListener {
            listener.onOrderShipClick(sumPrice)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateQuantity(quantity: TextView, position: Int) {
        quantity.text = String.format("%02d", quantityRooms[position])
        sumPriceRooms[position] = quantityRooms[position] * items[position].price
        sumPrice = sumPriceRooms.sum()
    }

    private fun String.toast() {
        Toast.makeText(context, this, Toast.LENGTH_LONG).show()
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }
}