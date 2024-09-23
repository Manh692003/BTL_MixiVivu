package com.emanh.mixivivu.view.airlineTicket

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.FragmentAirlineTicketBinding
import com.emanh.mixivivu.viewModel.PlaneViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AirlineTicketFragment : Fragment() {
    private var _binding: FragmentAirlineTicketBinding? = null

    private val binding get() = _binding!!
    private val viewModel = PlaneViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAirlineTicketBinding.inflate(inflater, container, false)

        initVideoIntro()
        initSearch()

        return binding.root
    }

    private fun initVideoIntro() {
        val videoPath = "android.resource://" + context?.packageName + "/" + R.raw.mixivivu_plane
        binding.videoIntro.setVideoURI(Uri.parse(videoPath))
        binding.videoIntro.setOnPreparedListener {mediaPlayer ->
            mediaPlayer.isLooping = true
            binding.videoIntro.start()
        }
    }

    private fun initSearch() {
        binding.oneWay.setChecked(true)

        val itemCity: Array<String> = resources.getStringArray(R.array.filterCity)
        val cityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, itemCity)
        binding.inputStartingPoint.setAdapter(cityAdapter)
        binding.inputDestination.setAdapter(cityAdapter)

        binding.inputDateGo.setOnClickListener {
            showDatePickerDialog(binding.inputDateGo)
        }

        binding.searchPlane.setOnClickListener {
            if (validate()) {
                initRecyclerView()
            }
        }
    }

    private fun initRecyclerView() {
        val newStartingPoint = removeText(binding.inputStartingPoint.text.toString())
        val newDestination = removeText(binding.inputDestination.text.toString())
        val intent = Intent(requireContext(), AirlineTicketFragment::class.java).apply {
            putExtra("DATE-GO", binding.inputDateGo.text.toString())
            putExtra("ADULT", binding.inputAdult.text.toString())
            putExtra("CHILDREN", binding.inputChildren.text.toString())
            putExtra("BABY", binding.inputBaby.text.toString())
        }

        viewModel.loadPlane(newStartingPoint, newDestination)
        binding.progressPlane.visibility = View.VISIBLE
        viewModel.plane.observe(viewLifecycleOwner) {
            binding.progressPlane.visibility = View.GONE
            if (it.isNullOrEmpty()) {
                binding.noData.visibility = View.VISIBLE
                binding.listPlane.visibility = View.GONE
            } else {
                binding.noData.visibility = View.GONE
                binding.listPlane.visibility = View.VISIBLE
                binding.listPlane.layoutManager = LinearLayoutManager(requireContext())
//                binding.listPlane.adapter = FlightAdapter(it, intent)
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean {
        val startingPoint = binding.inputStartingPoint.text.toString()
        val destination = binding.inputDestination.text.toString()
        val dateGo = binding.inputDateGo.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        var adult = binding.inputAdult.text.toString()
        var children = binding.inputChildren.text.toString()
        var baby = binding.inputBaby.text.toString()

        if (startingPoint.isEmpty()) {
            binding.inputStartingPoint.error = "Điểm đi không được để trống"
            "Vui lòng cung cấp điểm đi của bạn".toast()
            return false
        }

        if (destination.isEmpty()) {
            binding.inputDestination.error = "Điểm đến không được để trống"
            "Vui lòng cung cấp điểm đến của bạn".toast()
            return false
        }

        if (adult.isEmpty()) adult = 0.toString()

        if (adult < 0.toString()) {
            binding.inputAdult.error = "Số người lớn phải lớn hơn 0"
            "Vui lòng cung cấp số người lớn hợp lệ".toast()
            return false
        }

        if (children.isEmpty()) children = 0.toString()

        if (children < 0.toString()) {
            binding.inputChildren.error = "Số trẻ em phải lớn hơn 0"
            "Vui lòng cung cấp số trẻ em hợp lệ".toast()
            return false
        }

        if (baby.isEmpty()) baby = 0.toString()

        if (baby < 0.toString()) {
            binding.inputBaby.error = "Số em bé phải lớn hơn 0"
            "Vui lòng cung cấp số em bé hợp lệ".toast()
            return false
        }

        if (dateGo.isEmpty()) {
            binding.inputDateGo.error = "Ngày đi không được để trống"
            "Vui lòng cung cấp ngày đi của bạn".toast()
            return false
        }

        val selectedDate = dateFormat.parse(dateGo)
        if (selectedDate != null) {
            if (selectedDate.before(currentDate)) {
                binding.inputDateGo.error = "Lỗi nhập ngày đi"
                "Ngày đi không được nhỏ hơn ngày hiện tại".toast()
                return false
            }
        }

        return true
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
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

    private fun removeText(input: String): String {
        return input.replace(Regex("\\s*\\(.*?\\)\\s*"), "").trim()
    }
}