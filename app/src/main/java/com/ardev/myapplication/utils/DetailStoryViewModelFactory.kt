package com.ardev.myapplication.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardev.myapplication.ui.activity.detailActivity.DetailStoryViewModel
import com.ardev.myapplication.ui.activity.postActivity.PostStoryActivityViewModel

class DetailStoryViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(application) as T
            }
            modelClass.isAssignableFrom(PostStoryActivityViewModel::class.java) -> {
                PostStoryActivityViewModel(application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}