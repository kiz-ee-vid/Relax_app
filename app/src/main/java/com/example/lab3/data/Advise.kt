package com.example.lab3.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Advise(
    var mood: String?,
    var title: String?,
    var description: String?,
    var image: Int
    ): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(mood)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(image)
    }

    companion object CREATOR : Parcelable.Creator<Advise> {
        override fun createFromParcel(parcel: Parcel): Advise {
            return Advise(parcel)
        }

        override fun newArray(size: Int): Array<Advise?> {
            return arrayOfNulls(size)
        }
    }
}
