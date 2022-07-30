package com.example.lab3.domain

import com.example.lab3.data.ApiItem

interface IRepository {
    suspend fun sendResponse(post: String): ApiItem?

}