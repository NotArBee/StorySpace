package com.ardev.myapplication.ui.activity.postActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dataStore

class PostStoryActivityViewModel(application: Application) : ViewModel() {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)
    val userData = userPreferences.getUserData().asLiveData()
}