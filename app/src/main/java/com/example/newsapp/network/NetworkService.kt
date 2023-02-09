package com.example.newsapp.network

import com.example.newsapp.BuildConfig
import com.example.newsapp.models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_LINK = "https://newsapi.org/"
const val PAGE_PER_CATEGORY = 5
const val key = BuildConfig.API_KEY //API key from local.properties file, which is not exposed to public repository

interface NetworkService {

    @GET("v2/top-headlines")
    fun getNewsHeadlinesByCategory(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("apiKey") key: String
    ): Call<ApiResponse>

    @GET("v2/top-headlines")
    fun getSearchedNewsHeadlines(
        @Query("q") searchQuery: String,
        @Query("apiKey") key: String
    ): Call<ApiResponse>
}