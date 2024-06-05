package com.example.storiesw.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storiesw.data.retrofit.AuthConfig
import com.example.storiesw.data.retrofit.request.LoginRequest
import com.example.storiesw.data.retrofit.response.BasicResponse
import com.example.storiesw.data.retrofit.response.LoginResponse
import com.example.storiesw.utils.EspressoIdlingResource
import com.example.storiesw.utils.PreferenceManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loginComplete = MutableLiveData<Boolean>()
    val loginComplete: LiveData<Boolean> = _loginComplete

    private val preferenceManager = PreferenceManager(application)

    fun login(email:String, password:String) {
        val request = LoginRequest(
            email = email,
            password = password
        )

        _isLoading.value = true
        EspressoIdlingResource.increment()
        val client = AuthConfig.getApiService().login(request)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()?.error == false) {
                    Log.d("Login", "Success: ${response.body()?.message}")
                    saveLoginResponse(response.body())
                } else {
                    try {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, BasicResponse::class.java)
                        val errorMessage = errorBody.message
                        _errorMessage.value = errorMessage
                    } catch (e: Exception) {
                        Log.e("Register", "Error parsing error response body: ${e.message}")
                    }
                    _isLoading.value = false
                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = t.message
                EspressoIdlingResource.decrement()
            }

        })
    }

    private fun saveLoginResponse(response: LoginResponse?) {
        response?.let {
            Log.d("Login", "Token: ${it.data.token}")
            _isLoading.value = false
            preferenceManager.saveToken(it.data.token)
            preferenceManager.setLoggedIn(true)
            _loginComplete.value = true
            EspressoIdlingResource.decrement()
        }


    }

}