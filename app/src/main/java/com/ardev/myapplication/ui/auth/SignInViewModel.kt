package com.ardev.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import com.ardev.myapplication.data.response.StoryRepository

class SignInViewModel (private val repository: StoryRepository) : ViewModel(){
    fun login(email:String, password:String) = repository.login(email,password)
}