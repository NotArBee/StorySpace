package com.ardev.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import com.ardev.myapplication.data.response.StoryRepository

class SignUpViewModel (private val repository: StoryRepository) : ViewModel() {
    fun register(name:String, email:String, password:String) =
        repository.register(name,email,password)
}