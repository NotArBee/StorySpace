package com.ardev.myapplication.data.retrofit

import com.ardev.myapplication.data.response.DetailStoryResponse
import com.ardev.myapplication.data.response.LoginResponse
import com.ardev.myapplication.data.response.NewStoryResponse
import com.ardev.myapplication.data.response.RegisterResponse
import com.ardev.myapplication.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

        @GET("stories")
        suspend fun getStories(
            @Header("Authorization") token: String,
            @Query("page") page: Int,
            @Query("size") size: Int
        ): Response<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<NewStoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ): Call<StoryResponse>
}