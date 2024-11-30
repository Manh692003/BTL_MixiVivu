@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.detailYacht

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanh.mixivivu.databinding.FragmentReviewsBinding
import com.emanh.mixivivu.model.ReviewsModel
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.viewModel.ReviewsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewsFragment : Fragment() {
    private var _binding: FragmentReviewsBinding? = null

    private val binding get() = _binding!!

    private lateinit var yachtModel: YachtModel
    private lateinit var reviewsViewModel: ReviewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)

        initReviews()
        initRating()
        initSendReviews()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initReviews() {
        yachtModel = requireActivity().intent.getParcelableExtra("objectYacht")!!
        reviewsViewModel = ViewModelProvider(this)[ReviewsViewModel::class.java]
        reviewsViewModel.loadReviews(yachtModel.id.toString())

        binding.progressEvaluation.visibility = View.VISIBLE
        reviewsViewModel.reviews.observe(viewLifecycleOwner) {
            binding.listEvaluation.layoutManager = LinearLayoutManager(requireContext())
            binding.listEvaluation.adapter = ReviewsAdapter(it)
            binding.progressEvaluation.visibility = View.GONE
        }
    }

    private fun initRating() {
        binding.rating.rating = 0f
        binding.rating.stepSize = 1f
    }

    private fun initSendReviews() {
        binding.sendReviews.setOnClickListener {
            if (validate()) {
                val name = binding.inputYourName.text.toString()
                val rating = binding.rating.rating.toInt()
                val content = binding.inputContentReview.text.toString()
                val date = getCurrentDate()

                val newReviews = ReviewsModel(
                    name = name,
                    rating = rating,
                    content = content,
                    date = date
                )

                reviewsViewModel.addReviews(yachtModel.id.toString(), newReviews)
                clearInputFields()

                reviewsViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
                    message.toast(requireContext())
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun String.toast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean {
        val name = binding.inputYourName.text.toString()
        val rating = binding.rating.rating
        val content = binding.inputContentReview.text.toString()

        if (name.isEmpty()) {
            binding.inputYourName.error = "Họ và tên không được để trống"
            "Vui lòng cung cấp họ và tên của bạn".toast(requireContext())
            return false
        }
        if (rating == 0f) {
            "Vui lòng cung cấp đánh giá của bạn".toast(requireContext())
            return false
        }
        if (content.isEmpty()) {
            binding.inputContentReview.error = "Nội dung đánh giá không được để trống"
            "Vui lòng cung cấp đánh giá của bạn".toast(requireContext())
            return false
        }
        return true
    }

    private fun clearInputFields() {
        binding.inputYourName.text.clear()
        binding.rating.rating = 0f
        binding.inputContentReview.text.clear()
    }
}