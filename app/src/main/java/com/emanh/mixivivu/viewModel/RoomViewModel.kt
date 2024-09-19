package com.emanh.mixivivu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanh.mixivivu.model.TypeRoomModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RoomViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _room = MutableLiveData<MutableList<TypeRoomModel>>()

    val room: LiveData<MutableList<TypeRoomModel>> = _room

    fun loadRoom(shipId: String) {
        val ref = firebaseDatabase.getReference("ship/$shipId/typeRoomPrice")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<TypeRoomModel>()
                for (childSnapshot in snapshot.children) {
                    val room = childSnapshot.getValue(TypeRoomModel::class.java)
                    room?.let { lists.add(it) }
                }
                _room.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RoomViewModel", "Lỗi khi lấy danh sách loại phòng: ${error.message}")
            }
        })
    }
}