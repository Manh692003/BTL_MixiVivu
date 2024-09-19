@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanh.mixivivu.databinding.FragmentPriceRoomBinding
import com.emanh.mixivivu.helper.OnOrderShipClickListener
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.viewModel.RoomViewModel
import java.text.NumberFormat
import java.util.Locale

class PriceRoomFragment : Fragment(), OnOrderShipClickListener {
    private var _binding: FragmentPriceRoomBinding? = null

    private val binding get() = _binding!!
    private var sumPrice: Int = 0

    private lateinit var yachtModel: YachtModel
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceRoomBinding.inflate(inflater, container, false)

        initRoom()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onOrderShipClick(sumPriceRoom: Int) {
        this.sumPrice = sumPriceRoom
        binding.sumPrice.text = "${formatPrice(sumPriceRoom)} đ"
    }

    @SuppressLint("SetTextI18n")
    private fun initRoom() {
        yachtModel = requireActivity().intent.getParcelableExtra("objectYacht")!!
        roomViewModel = ViewModelProvider(this)[RoomViewModel::class.java]
        roomViewModel.loadRoom(yachtModel.id.toString())

        binding.progressPriceRoom.visibility = View.VISIBLE
        roomViewModel.room.observe(viewLifecycleOwner) {
            binding.listPriceRoom.layoutManager = LinearLayoutManager(requireContext())
            binding.listPriceRoom.adapter = PriceRoomAdapter(it, this)
            binding.progressPriceRoom.visibility = View.GONE
        }

        binding.sumPrice.text = "${formatPrice(sumPrice)} đ"

        binding.orderShip.setOnClickListener {
            if (sumPrice > 0) {
                val intent = Intent(requireContext(), OrderShipActivity::class.java)
                intent.putExtra("objectSumPrice", sumPrice)
                startActivity(intent)
            } else {
                "Hãy chọn phòng phù hợp đề đặt du thuyền".toast()
            }
        }
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }

    private fun String.toast() {
        Toast.makeText(context, this, Toast.LENGTH_LONG).show()
    }
}