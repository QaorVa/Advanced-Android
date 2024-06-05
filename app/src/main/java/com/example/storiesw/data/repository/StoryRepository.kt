package com.example.storiesw.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storiesw.data.StoryRemoteMediator
import com.example.storiesw.data.database.StoryDatabase
import com.example.storiesw.data.model.Story
import com.example.storiesw.data.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),

            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = { storyDatabase.storyDao().getAllStories() }
        ).liveData
    }
}