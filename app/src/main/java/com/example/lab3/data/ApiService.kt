package com.example.lab3.data

import com.example.lab2.domain.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("https://aztro.sameerkumar.website/")
    suspend fun sendResponse(@Query("sign") sign: String): Response<ApiItem>
}