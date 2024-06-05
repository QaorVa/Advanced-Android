package com.example.storiesw.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storiesw.R
import com.example.storiesw.data.retrofit.ApiConfig
import com.example.storiesw.data.retrofit.response.BasicResponse
import com.example.storiesw.utils.PreferenceManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddViewModel(application: Application): AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val preferenceManager = PreferenceManager(application)

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _uploadComplete = MutableLiveData<Boolean>()
    val uploadComplete: LiveData<Boolean> = _uploadComplete

    fun uploadStory(
        image: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ) {
        _isLoading.value = true

        val requestFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", image.name, requestFile)
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val latitudePart = latitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val longitudePart = longitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

        _isLoading.value = true
        val client = preferenceManager.getToken()?.let { ApiConfig.getApiService(it).addStory(photoPart, descriptionPart, latitudePart, longitudePart) }
        client?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.error  == false) {
                        _uploadComplete.value = true
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = response.body()?.message
                    }
                } else {
                    _isError.value = true
                    _errorMessage.value = getApplication<Application>().getString(R.string.upload_failed)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = t.message
            }
        })
    }
}