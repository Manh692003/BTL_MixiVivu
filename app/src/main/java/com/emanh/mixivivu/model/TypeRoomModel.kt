package com.emanh.mixivivu.model

import android.os.Parcel
import android.os.Parcelable

data class TypeRoomModel(
    val name: String = "",
    val area: Int = 0,
    val capacity: Int = 0,
    val price: Int = 0,
    var features: ArrayList<String> = ArrayList(),
    val banner: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
) : Parcelable {
    constructor(parcel: Parcel) : this(
    parcel.readString().toString(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.createStringArrayList() as ArrayList<String>,
    parcel.readString().toString(),
    parcel.createStringArrayList() as ArrayList<String>,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(area)
        parcel.writeInt(capacity)
        parcel.writeInt(price)
        parcel.writeStringList(features)
        parcel.writeString(banner)
        parcel.writeStringList(picUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypeRoomModel> {
        override fun createFromParcel(parcel: Parcel): TypeRoomModel {
            return TypeRoomModel(parcel)
        }

        override fun newArray(size: Int): Array<TypeRoomModel?> {
            return arrayOfNulls(size)
        }
    }
}
