package com.ardev.myapplication.ui.fragment.enterForm.signIn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardev.myapplication.data.response.LoginResponse
import com.ardev.myapplication.data.response.LoginResult
import com.ardev.myapplication.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {
    private val _signIn = MutableLiveData<LoginResponse>()
    val signIn: LiveData<LoginResponse> = _signIn

    private val _isSignInSuccessful = MutableLiveData<Boolean>()
    val isSignInSuccessful: LiveData<Boolean> = _isSignInSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signIn(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().signIn(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _signIn.value = response.body()
                    response.body()?.loginResult?.let {
                        saveLoginResult(it)
                    }
                    Log.d(TAG, "SignIn successful")
                    _isSignInSuccessful.value = true
                } else {
                    Log.e(TAG, "SignIn failed")
                    _isSignInSuccessful.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "SignIn failed: ${t.message}")
                _isSignInSuccessful.value = false
            }
        })
    }

    private fun saveLoginResult(loginResult: LoginResult) {
        // Code to save loginResult to datastore
        // For example, you can use Android's DataStore
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }
}
