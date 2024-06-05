package com.example.storiesw.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storiesw.data.retrofit.ApiConfig
import com.example.storiesw.data.retrofit.response.StoryDetailResponse
import com.example.storiesw.utils.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application)  {
    val storyDetail = MutableLiveData<StoryDetailResponse>()

    private val preferenceManager = PreferenceManager(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun setStoryDetail(id: String?) {
        _isLoading.value = true
        val client = preferenceManager.getToken()
            ?.let { ApiConfig.getApiService(it).getStoryDetail(id) }
        client?.enqueue(object : Callback<StoryDetailResponse> {
            override fun onResponse(
                call: Call<StoryDetailResponse>,
                response: Response<StoryDetailResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _isError.value = false
                    storyDetail.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.d("OnFailure", t.message.toString())
            }

        })
    }

    fun getStoryDetail(): LiveData<StoryDetailResponse> {
        return storyDetail
    }
}