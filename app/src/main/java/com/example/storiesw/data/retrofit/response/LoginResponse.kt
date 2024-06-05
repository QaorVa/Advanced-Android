package com.example.storiesw.data.retrofit.response

import com.example.storiesw.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("loginResult")
    val data: User
)
