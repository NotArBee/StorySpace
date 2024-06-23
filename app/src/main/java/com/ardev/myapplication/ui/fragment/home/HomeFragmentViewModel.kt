package com.ardev.myapplication.ui.fragment.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ardev.myapplication.data.paging.StoryPagingSource
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.data.retrofit.ApiConfig
import dataStore
import kotlinx.coroutines.flow.Flow

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)
    val userData = userPreferences.getUserData().asLiveData()

    fun getStories(token: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(ApiConfig.getApiService(), token) }
        ).flow.cachedIn(viewModelScope)
    }
}