package com.example.storiesw.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storiesw.data.model.Story
import com.example.storiesw.data.retrofit.ApiConfig
import com.example.storiesw.data.retrofit.response.StoriesResponse
import com.example.storiesw.utils.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(application: Application): AndroidViewModel(application)  {
    val listStory = MutableLiveData<List<Story>?>()

    val preferenceManager = PreferenceManager.getInstance(application)
    val token = preferenceManager.getToken() ?: ""

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun setStoriesLocation() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getAllStories(location = 1)
        client.enqueue(object: Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _isError.value = false
                    listStory.postValue(response.body()?.data)
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })

    }

    fun getStories(): LiveData<List<Story>?> {
        return listStory
    }
}