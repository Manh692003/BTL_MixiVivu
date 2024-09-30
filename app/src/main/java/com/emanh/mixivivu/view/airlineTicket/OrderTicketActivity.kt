package com.emanh.mixivivu.view.airlineTicket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ActivityOrderTicketBinding
import com.emanh.mixivivu.view.MainActivity
import com.emanh.mixivivu.viewModel.AirlineTicketViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.NumberFormat
import java.util.Locale

class OrderTicketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderTicketBinding
    private lateinit var dialog: BottomSheetDialog
    private val airlineTicketViewModel: AirlineTicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initSumPrice()
        initInputContent()
    }

    private fun init() {
        binding.buttonBack.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun initSumPrice() {
        val bundle = intent.extras
        if (bundle != null) {
            val sumPrice = bundle.getInt("key_sumPrice")
            binding.sumPrice.text = "${formatPrice(sumPrice)} đ"
        }
    }

    private fun initInputContent() {
        binding.orderShip.setOnClickListener {
            if (validate()) {
                val bundle = intent.extras
                if (bundle != null) {
                    val username = binding.inputYourName.text.toString()
                    val emailRegister = binding.inputYourEmail.text.toString()
                    val phoneNumber = binding.inputNumberPhone.text.toString()
                    val price = binding.sumPrice.text.toString()
                    val airline = bundle.getString("key_airline")
                    val seatClass = bundle.getString("key_seatClass")
                    val takeOffTime = bundle.getString("key_takeOffTime")
                    val timeline = bundle.getString("key_timeline")

                    airlineTicketViewModel.sendEmail(username, emailRegister, phoneNumber, price,
                        airline, seatClass, takeOffTime, timeline)
                }

                completeOrderRoom()
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(this@OrderTicketActivity, this, Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean {
        val name = binding.inputYourName.text.toString()
        val numberPhone = binding.inputNumberPhone.text.toString()
        val email = binding.inputYourEmail.text.toString()

        if (name.isEmpty()) {
            binding.inputYourName.error = "Họ và tên không được để trống"
            "Vui lòng cung cấp họ và tên của bạn".toast()
            return false
        }

        if (numberPhone.isEmpty()) {
            binding.inputNumberPhone.error = "Số điện thoại không được để trống"
            "Vui lòng cung cấp số điện thoại của bạn".toast()
            return false
        }

        if (!numberPhone.matches(Regex("^\\d{10}\$"))) {
            binding.inputNumberPhone.error = "Số điện thoại không hợp lệ"
            "Vui lòng cung cấp một số điện thoại hợp lệ".toast()
            return false
        }

        if (email.isEmpty()) {
            binding.inputYourEmail.error = "Email không được để trống"
            "Vui lòng cung cấp email của bạn".toast()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputYourEmail.error = "Định dạng email không hợp lệ"
            "Vui lòng cung cấp một email hợp lệ".toast()
            return false
        }

        return true
    }

    @SuppressLint("InflateParams")
    private fun completeOrderRoom() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_ticket, null)
        dialog = BottomSheetDialog(this, R.style.DialogTheme)
        dialog.setContentView(dialogView)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true

        val completeBooking = dialogView.findViewById<AppCompatButton>(R.id.completeBooking)
        completeBooking.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@OrderTicketActivity, MainActivity::class.java))
        }

        dialog.show()
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }
}