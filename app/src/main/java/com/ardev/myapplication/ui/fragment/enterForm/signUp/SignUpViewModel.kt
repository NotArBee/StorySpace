package com.ardev.myapplication.ui.fragment.enterForm.signUp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardev.myapplication.data.response.RegisterResponse
import com.ardev.myapplication.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<RegisterResponse>()
    val signUpResult: LiveData<RegisterResponse> = _signUpResult

    private val _isSignUpSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> = _isSignUpSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signUp(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _signUpResult.value = response.body()
                    Log.d(TAG, "SignUp successful")
                    _isSignUpSuccessful.value = true
                } else {
                    Log.e(TAG, "SignUp failed")
                    _isSignUpSuccessful.value = false
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "SignUp failed: ${t.message}")
                _isSignUpSuccessful.value = false
            }
        })
    }

    companion object {
        private const val TAG = "SignUpViewModel"
    }
}
