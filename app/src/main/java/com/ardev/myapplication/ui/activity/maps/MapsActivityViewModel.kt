package com.ardev.myapplication.ui.activity.maps

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.data.response.StoryResponse
import com.ardev.myapplication.data.retrofit.ApiConfig
import dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivityViewModel(application: Application) : ViewModel() {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)
    val userData = userPreferences.getUserData().asLiveData()

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    fun getMarkerData(token: String) {
        val client = ApiConfig.getApiService().getStoriesWithLocation(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    _story.value = response.body()?.listStory
                    Log.d(TAG, "Succes get Marker: ${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, response: Throwable) {
                Log.e(TAG, "onFailure: ${response.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MapsActivityViewModel"
    }
}