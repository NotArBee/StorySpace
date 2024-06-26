package com.ardev.myapplication.data.response

import android.content.Context
import com.ardev.myapplication.data.database.StoryDatabase
import com.ardev.myapplication.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository.getInstance(apiService, database)
    }
}