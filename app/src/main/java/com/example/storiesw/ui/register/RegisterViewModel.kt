package com.example.storiesw.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storiesw.data.retrofit.AuthConfig
import com.example.storiesw.data.retrofit.request.RegisterRequest
import com.example.storiesw.data.retrofit.response.BasicResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(application: Application): AndroidViewModel(application)  {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _registerComplete = MutableLiveData<Boolean>()
    val registerComplete: LiveData<Boolean> = _registerComplete

    fun register(name: String, email:String, password:String) {
        val request = RegisterRequest(
            name = name,
            email = email,
            password = password
        )
        _isLoading.value = true
        val client = AuthConfig.getApiService().register(request)
        client.enqueue(object: Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                _isLoading.value = false
                if(response.body()?.error == false) {
                    Log.d("Register", "Success: ${response.body()?.message}")
                    _registerComplete.value = true
                } else {
                    try {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, BasicResponse::class.java)
                        val errorMessage = errorBody.message
                        Log.d("Register", "Error: $errorMessage")
                        _errorMessage.value = errorMessage
                    } catch (e: Exception) {
                        Log.e("Register", "Error parsing error response body: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }

        })
    }

}