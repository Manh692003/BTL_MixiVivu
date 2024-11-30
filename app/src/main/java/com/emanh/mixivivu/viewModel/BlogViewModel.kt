package com.emanh.mixivivu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanh.mixivivu.model.BlogModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _blog = MutableLiveData<MutableList<BlogModel>>()

    val blog: LiveData<MutableList<BlogModel>> = _blog

    fun loadBlog() {
        val ref = firebaseDatabase.getReference("blog")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BlogModel>()
                for (childSnapshot in snapshot.children) {
                    val blog = childSnapshot.getValue(BlogModel::class.java)
                    blog?.let { lists.add(it) }
                }
                _blog.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BlogViewModel", "Lỗi khi lấy danh sách blog: ${error.message}")
            }
        })
    }
}