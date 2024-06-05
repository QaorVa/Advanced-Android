package com.example.storiesw.data.retrofit

import com.example.storiesw.data.retrofit.request.LoginRequest
import com.example.storiesw.data.retrofit.request.RegisterRequest
import com.example.storiesw.data.retrofit.response.BasicResponse
import com.example.storiesw.data.retrofit.response.LoginResponse
import com.example.storiesw.data.retrofit.response.StoriesResponse
import com.example.storiesw.data.retrofit.response.StoryDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getAllStoriesPaging(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoriesResponse

    @GET("stories/{id}")
    fun getStoryDetail(
        @Path("id") id: String?
    ): Call<StoryDetailResponse>

    @POST("stories")
    @Multipart
    fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): Call<BasicResponse>

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body request: RegisterRequest
    ): Call<BasicResponse>
}