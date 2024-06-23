package com.ardev.myapplication.ui.activity.postActivity

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.ardev.myapplication.R
import com.ardev.myapplication.data.response.NewStoryResponse
import com.ardev.myapplication.data.retrofit.ApiConfig
import com.ardev.myapplication.utils.reduceFileImage
import com.ardev.myapplication.utils.uriToFile
import com.google.gson.Gson
import dataStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PostStoryActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)
    val userData = userPreferences.getUserData().asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<String>()
    val uploadResult: LiveData<String> = _uploadResult

    fun uploadImage(
        uri: Uri,
        description: String,
        token: String?
    ) {
        val imageFile = uriToFile(uri, getApplication()).reduceFileImage()
        Log.d("Image File", "showImage: ${imageFile.path}")

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        _isLoading.value = true

        val client = ApiConfig.getApiService().uploadStories(
            description = requestBody,
            photo = multipartBody,
            token = "Bearer $token"
        )

        client.enqueue(object : Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _uploadResult.value = response.body()?.message
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, NewStoryResponse::class.java)
                    _uploadResult.value = errorResponse.message
                }
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                if (t is IOException) {
                    _uploadResult.value = getApplication<Application>().getString(R.string.network_error)
                } else {
                    _uploadResult.value = getApplication<Application>().getString(R.string.unknown_error)
                }
            }
        })
    }
}
