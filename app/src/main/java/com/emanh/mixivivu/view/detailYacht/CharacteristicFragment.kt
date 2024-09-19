@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.detailYacht

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanh.mixivivu.databinding.FragmentCharacteristicBinding
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.viewModel.YachtViewModel

class CharacteristicFragment : Fragment() {
    private var _binding: FragmentCharacteristicBinding? = null

    private val binding get() = _binding!!

    private lateinit var yachtModel: YachtModel
    private lateinit var yachtViewModel: YachtViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacteristicBinding.inflate(inflater, container, false)

        initListHighlights()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListHighlights() {
        yachtModel = requireActivity().intent.getParcelableExtra("objectYacht")!!
        yachtViewModel = ViewModelProvider(this)[YachtViewModel::class.java]
        yachtViewModel.loadShip("", "", Int.MIN_VALUE, Int.MAX_VALUE)

        val listHighlights =  ArrayList<String>()
        for (highlight in yachtModel.characteristic) {
            listHighlights.add(highlight)
        }

        binding.progressHighlights.visibility = View.VISIBLE
        yachtViewModel.ship.observe(viewLifecycleOwner) {
            binding.listHighlights.layoutManager = LinearLayoutManager(requireContext())
            binding.listHighlights.adapter = HighlightsAdapter(listHighlights)
            binding.progressHighlights.visibility = View.GONE
        }
    }
}