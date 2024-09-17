package com.emanh.mixivivu.model

import android.os.Parcel
import android.os.Parcelable

data class YachtModel(
    var id: Int = 0,
    var title: String = "",
    var price: Int = 0,
    var location: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var characteristic: ArrayList<String> = ArrayList(),
    var infoShip: ArrayList<String> = ArrayList(),
    var typeRoomPrice: ArrayList<TypeRoomModel> = ArrayList(),
    var introduceText: ArrayList<String> = ArrayList(),
    var introducePicUrl: ArrayList<String> = ArrayList(),
    var evaluate: ArrayList<ReviewsModel> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createTypedArrayList(TypeRoomModel.CREATOR) as ArrayList<TypeRoomModel>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createTypedArrayList(ReviewsModel.CREATOR) as ArrayList<ReviewsModel>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(price)
        parcel.writeString(location)
        parcel.writeStringList(picUrl)
        parcel.writeStringList(characteristic)
        parcel.writeStringList(infoShip)
        parcel.writeTypedList(typeRoomPrice)
        parcel.writeStringList(introduceText)
        parcel.writeStringList(introducePicUrl)
        parcel.writeTypedList(evaluate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YachtModel> {
        override fun createFromParcel(parcel: Parcel): YachtModel {
            return YachtModel(parcel)
        }

        override fun newArray(size: Int): Array<YachtModel?> {
            return arrayOfNulls(size)
        }
    }
}
