package com.ardev.myapplication.ui.fragment.enterForm.signIn

import UserPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ardev.myapplication.data.response.LoginResponse
import com.ardev.myapplication.data.response.LoginResult
import com.ardev.myapplication.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _signIn = MutableLiveData<LoginResponse>()
    val signIn: LiveData<LoginResponse> get() = _signIn

    private val _isSignInSuccessful = MutableLiveData<Boolean>()
    val isSignInSuccessful: LiveData<Boolean> get() = _isSignInSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun signIn(email: String, password: String) {
        _isLoading.value = true
        ApiConfig.getApiService().signIn(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        _signIn.value = loginResponse
                        loginResponse.loginResult?.let { loginResult ->
                            viewModelScope.launch {
                                try {
                                    _isSignInSuccessful.value = true
                                    Log.d(TAG, "SignIn successful")
                                } catch (e: Exception) {
                                    _isSignInSuccessful.value = false
                                    Log.e(TAG, "SignIn failed: ${e.message}")
                                }
                            }
                        }
                    } ?: run {
                        _isSignInSuccessful.value = false
                        Log.e(TAG, "SignIn failed: Empty response body")
                    }
                } else {
                    _isSignInSuccessful.value = false
                    Log.e(TAG, "SignIn failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isSignInSuccessful.value = false
                Log.e(TAG, "SignIn failed: ${t.message}")
            }
        })
    }

    fun getUserData() : LiveData<LoginResult?> {
        return userPreferences.getUserData().asLiveData()
    }

    fun saveUserData(userData: LoginResult) {
        viewModelScope.launch {
            userPreferences.saveUserData(userData)
        }
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }
}