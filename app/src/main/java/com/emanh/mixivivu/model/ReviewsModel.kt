package com.emanh.mixivivu.model

import android.os.Parcel
import android.os.Parcelable

data class ReviewsModel(
    val name: String = "",
    val rating: Int = 0,
    val content: String = "",
    val date: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(rating)
        parcel.writeString(content)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReviewsModel> {
        override fun createFromParcel(parcel: Parcel): ReviewsModel {
            return ReviewsModel(parcel)
        }

        override fun newArray(size: Int): Array<ReviewsModel?> {
            return arrayOfNulls(size)
        }
    }
}
