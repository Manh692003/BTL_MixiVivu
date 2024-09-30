package com.emanh.mixivivu.model

import android.os.Parcel
import android.os.Parcelable

data class BlogModel(
    var id: Int = 0,
    var title: String = "",
    var date: String = "",
    var intro: String = "",
    var content: ArrayList<String> = ArrayList(),
    var picList: ArrayList<String> = ArrayList(),
    var endContent: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(intro)
        parcel.writeStringList(content)
        parcel.writeStringList(picList)
        parcel.writeString(endContent)
    }

    companion object CREATOR : Parcelable.Creator<BlogModel> {
        override fun createFromParcel(parcel: Parcel): BlogModel {
            return BlogModel(parcel)
        }

        override fun newArray(size: Int): Array<BlogModel?> {
            return arrayOfNulls(size)
        }
    }
}
