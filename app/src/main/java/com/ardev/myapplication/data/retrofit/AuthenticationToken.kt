package com.ardev.myapplication.data.retrofit

import android.content.Context
import com.ardev.myapplication.data.pref.PreferenceDataSource
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationToken (context: Context) : Interceptor {
    private val sessionManager = PreferenceDataSource.invoke(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}