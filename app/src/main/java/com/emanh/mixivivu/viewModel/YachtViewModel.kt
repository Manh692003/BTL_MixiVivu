package com.emanh.mixivivu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanh.mixivivu.model.YachtModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YachtViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _ship = MutableLiveData<MutableList<YachtModel>>()

    val ship: LiveData<MutableList<YachtModel>> = _ship

    fun loadShip(inputShip: String, location: String, minPrice: Int, maxPrice: Int) {
        val ref = firebaseDatabase.getReference("ship")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<YachtModel>()
                for (childSnapshot in snapshot.children) {
                    val ship = childSnapshot.getValue(YachtModel::class.java)
                    ship?.let {
                        if (it.title.contains(inputShip, ignoreCase = true) &&
                            it.location.contains(location, ignoreCase = true) &&
                            it.price in minPrice..maxPrice) {
                            lists.add(it)
                        }
                    }
                }
                _ship.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RoomViewModel", "Lỗi khi lấy danh sách loại phòng: ${error.message}")
            }
        })
    }
}