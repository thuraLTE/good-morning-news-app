package com.example.newsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_LINK)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}