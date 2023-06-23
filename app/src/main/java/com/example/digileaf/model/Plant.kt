package com.example.digileaf.model

import android.os.Parcel
import android.os.Parcelable

class Plant(
    var plantName: String,
    var plantAge: String,
    var plantSpecies: String,
    var plantDescription: String,
    var plantImage: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(plantName)
        parcel.writeString(plantAge)
        parcel.writeString(plantSpecies)
        parcel.writeString(plantDescription)
        parcel.writeInt(plantImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Plant> {
        override fun createFromParcel(parcel: Parcel): Plant {
            return Plant(parcel)
        }

        override fun newArray(size: Int): Array<Plant?> {
            return arrayOfNulls(size)
        }
    }

}