package com.example.storiesw.data.retrofit.response

import com.example.storiesw.data.model.Story
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("listStory")
    val data: List<Story>
)