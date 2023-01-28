package com.example.newsapp.network

import com.example.newsapp.models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_LINK = "https://newsapi.org/"
const val API_KEY = "69098ca7575f42069325e5168a565e07"
const val PAGE_PER_CATEGORY = 5

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