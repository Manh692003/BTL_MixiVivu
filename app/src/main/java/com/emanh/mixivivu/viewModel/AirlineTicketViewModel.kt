package com.emanh.mixivivu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanh.mixivivu.model.AirlineTicketModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AirlineTicketViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _airlineTicket = MutableLiveData<MutableList<AirlineTicketModel>>()
    val airlineTicket: LiveData<MutableList<AirlineTicketModel>> get() = _airlineTicket

    fun loadPlane(inputStartingPoint: String, inputDestination: String) {
        val ref = firebaseDatabase.getReference("plane")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<AirlineTicketModel>()
                for (childSnapshot in snapshot.children) {
                    val airlineTicket = childSnapshot.getValue(AirlineTicketModel::class.java)
                    airlineTicket?.let {
                        if (it.startingPoint.contains(inputStartingPoint, ignoreCase = true) &&
                            it.destination.contains(inputDestination, ignoreCase = true)) {
                            lists.add(it)
                        }
                    }
                }
                _airlineTicket.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PlaneViewModel", "Lỗi khi lấy danh sách airlineTicket: ${error.message}")
            }
        })
    }

    fun sendEmail(username: String, receiverEmail: String, phoneNumber: String, price: String,
                  airline: String?, seatClass: String?, takeOffTime: String?, timeline: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val senderEmail = "phankhacmanh6903@gmail.com"
            val senderPassword = "kejx grwa fwxx zkcj"
            val stringHost = "smtp.gmail.com"

            val properties = Properties().apply {
                put("mail.smtp.host", stringHost)
                put("mail.smtp.port", "465")
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.auth", "true")
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(senderEmail))
                    addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
                    subject = "Xác nhận thông tin vé máy bay MixiVivu"
                    setText("Chúng tôi nhận được thông tin đặt vé máy bay từ khách hàng $username với tài khoản email là: $receiverEmail." +
                            "\nXác nhận đặt vé máy bay thông qua số điện thoại $phoneNumber với số tiền là $price VNĐ." +
                            "\n\nThông tin vé:" +
                            "\n\tHãng bay: $airline" +
                            "\n\tHạng ghế: $seatClass" +
                            "\n\tThời gian khởi hành: $takeOffTime" +
                            "\n\tThời gian bay: $timeline phút" +
                            "\n\nThông tin thanh toán đặt phòng du thuyền qua số tài khoản: 0394817283 - BIDV - MixiVivu." +
                            "\nMọi thắng mắc xin liên hệ đến số điện thoại: 0394817283." +
                            "\n\n-----------------------------" +
                            "\n\nTrân trong cảm ơn quý khách!")
                }

                Transport.send(message)
                println("Email sent successfully!")
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}