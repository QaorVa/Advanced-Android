package com.example.storiesw.di

import android.content.Context
import com.example.storiesw.data.database.StoryDatabase
import com.example.storiesw.data.repository.StoryRepository
import com.example.storiesw.data.retrofit.ApiConfig
import com.example.storiesw.utils.PreferenceManager

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val preferenceManager = PreferenceManager.getInstance(context)
        val token = preferenceManager.getToken() ?: ""
        val apiService = ApiConfig.getApiService(token)
        return StoryRepository(database, apiService)
    }
}