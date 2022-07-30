package com.example.lab3.data

import com.example.lab3.domain.IRepository
import com.example.lab3.data.ApiItem
import com.example.lab3.data.ApiService

class RepositoryImpl (private val apiService: ApiService) : IRepository {

    override suspend fun sendResponse(post: String): ApiItem?{
        return apiService.sendResponse(post).body()
    }

}