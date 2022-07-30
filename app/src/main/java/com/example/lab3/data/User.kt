package com.example.lab3.data

import android.net.Uri

data class User(var login: String, var list: MutableList<String>, var name: String, var mood: String, var image: String?) {
    constructor(): this("Not authorized", mutableListOf(), "Noname", "No information", null)
}