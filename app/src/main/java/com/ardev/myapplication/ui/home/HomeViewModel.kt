package com.ardev.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ardev.myapplication.data.database.StoryEntity
import com.ardev.myapplication.data.response.StoryRepository

class HomeViewModel (repository: StoryRepository) : ViewModel() {
    val listStoryResponse : LiveData<PagingData<StoryEntity>> =
        repository.allStories().cachedIn(viewModelScope)
}