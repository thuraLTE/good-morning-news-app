package com.example.newsapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.models.ApiResponse
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsCategory
import com.example.newsapp.network.API_KEY
import com.example.newsapp.network.PAGE_PER_CATEGORY
import com.example.newsapp.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val viewModelTag = "News ViewModel"

    private var _articleListLiveData = MutableLiveData<List<Article>>()
    val articleListLiveData: LiveData<List<Article>> = _articleListLiveData

    private var _currentCategory = MutableLiveData<String>(NewsCategory.GENERAL.category)
    val currentCategory: LiveData<String> = _currentCategory

    private var _currentQuery = MutableLiveData<String>("")
    val currentQuery: LiveData<String> = _currentQuery

    private var _isQueryChangedLiveData = MutableLiveData<Boolean>(false)
    val isQueryChangedLiveData: LiveData<Boolean> = _isQueryChangedLiveData

    fun getNewsHeadlinesByCategory(category: String) {
        RetrofitInstance.retrofitService.getNewsHeadlinesByCategory(category, PAGE_PER_CATEGORY, API_KEY)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    response.body()?.let { apiResponse ->
                        val fetchedArticles = apiResponse.articles
                        Log.d(viewModelTag, "${fetchedArticles.size}")
                        _articleListLiveData.value = emptyList()
                        _articleListLiveData.value = fetchedArticles.toList()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e(viewModelTag, t.message.toString())
                }
            })
    }

    fun getSearchedNewsHeadlines(query: String) {
        RetrofitInstance.retrofitService.getSearchedNewsHeadlines(query, API_KEY)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    response.body()?.let { apiResponse ->
                        val fetchedArticles = apiResponse.articles
                        Log.d(viewModelTag, "${fetchedArticles.size}")
                        _articleListLiveData.value = emptyList()
                        _articleListLiveData.value = fetchedArticles.toList()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e(viewModelTag, t.message.toString())
                }

            })
    }

    fun changeCategory(category: String) {
        _currentCategory.value = category
    }

    fun changeQuery(query: String) {
        _currentQuery.value = query
    }

    fun changeQueryStatus() {
        _isQueryChangedLiveData.value = true
    }
}