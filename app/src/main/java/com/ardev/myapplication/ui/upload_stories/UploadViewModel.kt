package com.ardev.myapplication.ui.upload_stories

import androidx.lifecycle.ViewModel
import com.ardev.myapplication.data.response.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: StoryRepository) : ViewModel(){
    fun uploadStories(file : MultipartBody.Part, description: RequestBody) =
        repository.uploadStories(file, description)
}