package com.emanh.mixivivu.model

import android.os.Parcel
import android.os.Parcelable

data class AirlineTicketModel(
    var id: Int = 0,
    var startingPoint: String = "",
    var destination: String = "",
    var namePlane: String = "",
    var airline: String = "",
    var picLogo: String = "",
    var takeOffTime: String = "",
    var landingTime: String = "",
    var seatClass: String = "",
    var luggage: String = "",
    var ticketPrice: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(pracel: Parcel, flags: Int) {
        pracel.writeInt(id)
        pracel.writeString(startingPoint)
        pracel.writeString(destination)
        pracel.writeString(namePlane)
        pracel.writeString(airline)
        pracel.writeString(picLogo)
        pracel.writeString(takeOffTime)
        pracel.writeString(landingTime)
        pracel.writeString(seatClass)
        pracel.writeString(luggage)
        pracel.writeInt(ticketPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AirlineTicketModel> {
        override fun createFromParcel(parcel: Parcel): AirlineTicketModel {
            return AirlineTicketModel(parcel)
        }

        override fun newArray(size: Int): Array<AirlineTicketModel?> {
            return arrayOfNulls(size)
        }
    }
}
