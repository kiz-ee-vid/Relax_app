package com.example.lab3.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Advise(
    var mood: String?,
    var title: String?,
    var description: String?,
    var image: Int
    ): Serializable

