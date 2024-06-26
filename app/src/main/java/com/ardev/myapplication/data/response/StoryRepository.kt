package com.ardev.myapplication.data.response

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ardev.myapplication.data.database.StoryDatabase
import com.ardev.myapplication.data.database.StoryEntity
import com.ardev.myapplication.data.retrofit.ApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService: ApiServices,
    private val database: StoryDatabase
) {
    companion object {
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiServices,
            database: StoryDatabase
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, database)
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.login(email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Story Repository", "login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.register(name, email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }

    }


    fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.uploadStories(file, description)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Upload", e.message.toString())
            emit(Result.Error(e.toString()))
        }
    }


    fun allStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    fun locationStory(): LiveData<Result<List<GetStoryResult>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.allStories(location = 5)
            val listStory = response.listStory
            emit(Result.Success(listStory))
        } catch (e: Exception) {
            Log.d("Maps", e.message.toString())
            emit(Result.Error(e.toString()))
        }

    }

}