package com.example.storiesw.data.retrofit

import com.example.storiesw.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthConfig {
    companion object {
        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}