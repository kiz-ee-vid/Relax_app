package com.example.lab3.ui.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.data.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val reference by lazy { DB.getReference() }
    private val auth by lazy { Firebase.auth }
    val user = MutableLiveData<User>()

    fun getUser(hash: String) {
        reference.child("users").child(hash).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(User::class.java)!!
                user.value = temp
//                user.value?.list = temp.list
//                user.value?.login = temp.login
//                user.value?.name = temp.name
//                user.value?.mood = temp.mood
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    }

    fun saveData(image: String){
        if (!user.value?.list?.contains(image)!!) {
            user.value!!.list.add(image)
            reference.child("users").child(user.value!!.login.hashCode().toString()).child("list")
                .setValue(user.value!!.list)
        }
    }

    fun saveList(){
        reference.child("users").child(user.value!!.login.hashCode().toString()).child("list")
            .setValue(user.value!!.list)
    }

    fun saveMood(){
        reference.child("users").child(user.value!!.login.hashCode().toString()).child("mood")
            .setValue(user.value!!.mood)
    }

    fun saveImage(){
        reference.child("users").child(user.value!!.login.hashCode().toString()).child("image")
            .setValue(user.value!!.image)
    }
}