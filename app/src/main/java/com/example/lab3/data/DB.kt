package com.example.lab3.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DB {
    private var reference: DatabaseReference? = null
    fun getReference(): DatabaseReference {
        if(reference == null)
            return FirebaseDatabase.getInstance().reference
        return reference!!
    }
}