package com.ardev.myapplication.ui.activity.detailActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ardev.myapplication.data.response.DetailStoryResponse
import com.ardev.myapplication.data.response.Story
import com.ardev.myapplication.data.retrofit.ApiConfig
import dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(application: Application) : ViewModel() {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)
    val userData = userPreferences.getUserData().asLiveData()

    private val _detailStory = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailStory
    
    fun getDetailStories(token: String, id: String) {
        val client = ApiConfig.getApiService().getDetailStories(token, id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _detailStory.value = response.body()?.story
                    Log.d(TAG, "Succes Load Detail Story : ${response.body().toString()}")
                } else {
                    Log.e(TAG, "Failed Load Detail Story : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, response: Throwable) {
                Log.e(TAG, "onFailure : ${response.message}")
            }

        })
    }

    companion object {
        private const val TAG = "DetailStoryViewModel"
    }
}