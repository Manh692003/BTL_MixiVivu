package com.emanh.mixivivu.view.booking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ActivityOrderShipBinding
import com.emanh.mixivivu.view.BaseActivity
import com.emanh.mixivivu.view.MainActivity
import com.emanh.mixivivu.viewModel.RoomViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderShipActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderShipBinding
    private lateinit var dialog: BottomSheetDialog
    private val roomViewModel: RoomViewModel by viewModels()

    private var sumPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderShipBinding.inflate(layoutInflater)
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
        sumPrice = intent.getIntExtra("objectSumPrice", 0)
        binding.sumPrice.text = "${formatPrice(sumPrice)} đ"
    }

    private fun initInputContent() {
        binding.inputDate.setOnClickListener {
            showDatePickerDialog(binding.inputDate)
        }

        binding.orderShip.setOnClickListener {
            if (validate()) {
                val username = binding.inputYourName.text.toString()
                val emailRegister = binding.inputYourEmail.text.toString()
                val phoneNumber = binding.inputNumberPhone.text.toString()
                val price = binding.sumPrice.text.toString()

                roomViewModel.sendEmail(username, emailRegister, phoneNumber, price)
                completeOrderRoom()
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(this@OrderShipActivity, this, Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean {
        val name = binding.inputYourName.text.toString()
        val numberPhone = binding.inputNumberPhone.text.toString()
        val email = binding.inputYourEmail.text.toString()
        val dateStr = binding.inputDate.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time

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

        if (dateStr.isEmpty()) {
            binding.inputDate.error = "Ngày nhận phòng không được để trống"
            "Vui lòng cung cấp ngày nhận phòng của bạn".toast()
            return false
        }

        val selectedDate = dateFormat.parse(dateStr)
        if (selectedDate != null) {
            if (selectedDate.before(currentDate)) {
                binding.inputDate.error = "Lỗi nhập ngày nhận phòng"
                "Ngày nhận phòng không được nhỏ hơn ngày hiện tại".toast()
                return false
            }
        }

        return true
    }

    @SuppressLint("InflateParams")
    private fun completeOrderRoom() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_room, null)
        dialog = BottomSheetDialog(this, R.style.DialogTheme)
        dialog.setContentView(dialogView)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true

        val completeBooking = dialogView.findViewById<AppCompatButton>(R.id.completeBooking)
        completeBooking.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@OrderShipActivity, MainActivity::class.java))
        }

        dialog.show()
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}