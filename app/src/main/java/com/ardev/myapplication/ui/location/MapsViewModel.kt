package com.ardev.myapplication.ui.location

import androidx.lifecycle.ViewModel
import com.ardev.myapplication.data.response.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getLocation() = repository.locationStory()
}