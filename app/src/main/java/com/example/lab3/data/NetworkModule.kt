package com.example.lab3.data

import com.example.lab2.domain.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    fun provideApiService(retrofit: Retrofit): ApiService = retrofit
        .create(ApiService::class.java)


    val okhttpclient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)

    fun provideProductRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL_PRODUCT)
            .callFactory(okhttpclient.build())
            .build()
    }

}