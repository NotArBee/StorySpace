package com.ardev.myapplication.data.retrofit

import com.ardev.myapplication.data.response.LoginResponse
import com.ardev.myapplication.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}