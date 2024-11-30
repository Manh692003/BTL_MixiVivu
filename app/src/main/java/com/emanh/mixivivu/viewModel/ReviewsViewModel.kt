@file:Suppress("NAME_SHADOWING")

package com.emanh.mixivivu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanh.mixivivu.model.ReviewsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewsViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _reviews = MutableLiveData<MutableList<ReviewsModel>>()
    val reviews: LiveData<MutableList<ReviewsModel>> get() = _reviews

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    data class ReviewsSummary(val count: Int, val averageRating: Double)
    private val _reviewsSummary = MutableLiveData<ReviewsSummary>()
    val reviewsSummary: LiveData<ReviewsSummary> get() = _reviewsSummary

    fun loadReviews(shipId: String) {
        val ref = firebaseDatabase.getReference("ship/$shipId/evaluate")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ReviewsModel>()
                for (childSnapshot in snapshot.children) {
                    val review = childSnapshot.getValue(ReviewsModel::class.java)
                    review?.let { lists.add(it) }
                }
                _reviews.value = lists

                updateReviewsSummary(lists)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReviewsViewModel", "loadReviews:onCancelled", error.toException())
            }
        })
    }

    fun addReviews(shipId: String, review: ReviewsModel) {
        val ref = firebaseDatabase.getReference("ship/$shipId/evaluate")
        ref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val maxId = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: -1
                val newId = maxId + 1

                ref.child(newId.toString()).setValue(review)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _toastMessage.value = "Gửi đánh giá thành công"
                        } else {
                            _toastMessage.value = "Gửi đánh giá thất bại"

                            val errorMessage = task.exception?.message ?: "Lỗi không xác định"
                            Log.e("ReviewsViewModel", "Lỗi khi gửi đánh giá: $errorMessage")
                        }
                    }
            } else {
                val errorMessage = task.exception?.message ?: "Lỗi không xác định"
                Log.e("ReviewsViewModel", "Lỗi khi lấy danh sách đánh giá: $errorMessage")
            }
        }
    }

    private fun updateReviewsSummary(reviews: MutableList<ReviewsModel>) {
        val count = reviews.size
        val averageRating = if (count > 0) reviews.sumOf { it.rating.toDouble() } / count else 0.0
        _reviewsSummary.value = ReviewsSummary(count, averageRating)
    }
}