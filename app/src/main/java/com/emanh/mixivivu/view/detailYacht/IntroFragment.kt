@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.detailYacht

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emanh.mixivivu.databinding.FragmentIntroBinding
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.viewModel.YachtViewModel

class IntroFragment : Fragment() {
    private var _binding: FragmentIntroBinding? = null

    private val binding get() = _binding!!

    private lateinit var yacht: YachtModel
    private lateinit var yachtViewModel: YachtViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)

        initIntro()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initIntro() {
        yacht = requireActivity().intent.getParcelableExtra("objectYacht")!!
        yachtViewModel = ViewModelProvider(this)[YachtViewModel::class.java]
        yachtViewModel.loadShip("", "", Int.MIN_VALUE, Int.MAX_VALUE)

        binding.progressIntro.visibility = View.VISIBLE
        yachtViewModel.ship.observe(viewLifecycleOwner) {
            binding.introContent1.text = yacht.introduceText[0]
            binding.introContent2.text = yacht.introduceText[1]
            binding.introContent3.text = yacht.introduceText[2]

            Glide.with(this)
                .load(yacht.introducePicUrl[0])
                .into(binding.introPic1)

            Glide.with(this)
                .load(yacht.introducePicUrl[1])
                .into(binding.introPic2)

            Glide.with(this)
                .load(yacht.introducePicUrl[2])
                .into(binding.introPic3)

            binding.introContent1.visibility = View.VISIBLE
            binding.introContent2.visibility = View.VISIBLE
            binding.introContent3.visibility = View.VISIBLE
            binding.introPic1.visibility = View.VISIBLE
            binding.introPic2.visibility = View.VISIBLE
            binding.introPic3.visibility = View.VISIBLE
            binding.progressIntro.visibility = View.GONE
        }
    }
}